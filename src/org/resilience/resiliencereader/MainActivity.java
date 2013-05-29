package org.resilience.resiliencereader;

import org.resilience.resiliencereader.entities.Article;
import org.resilience.resiliencereader.entities.ArticleList;
import org.resilience.resiliencereader.entities.ArticlesFeed;
import org.resilience.resiliencereader.entities.FeedType;
import org.resilience.resiliencereader.entities.Feeds;
import org.resilience.resiliencereader.entities.OnSaveArticleImageListener;
import org.resilience.resiliencereader.entities.OnSaveArticleStrippedDescriptionListener;
import org.resilience.resiliencereader.entities.ResourcesFeed;
import org.resilience.resiliencereader.framework.ArticleImageSaver;
import org.resilience.resiliencereader.framework.ArticleStrippedDescriptionSaver;
import org.resilience.resiliencereader.framework.ArticlesContentProvider;
import org.resilience.resiliencereader.framework.ArticlesSQLiteOpenHelper;
import org.resilience.resiliencereader.framework.FeedDownloader;
import org.resilience.resiliencereader.framework.OnFeedDownloaderDoneListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

//TODO: When do we want automatic refresh of the feed?

public class MainActivity extends SherlockFragmentActivity implements OnFeedDownloaderDoneListener,
      OnArticleSelectedListener, FeedTabListenerActivity
{

   public final static String PREFERENCES_FILENAME = "preferences.xml";
   public final static String PREFERRED_ARTICLE_FEED = "articles_feed_preference";
   public final static String PREFERRED_RESOURCE_FEED = "resources_feed_preference";
   public final static String ARTICLES_KEY = "articles";
   public final static String RESOURCES_KEY = "resources";
   public final static String ARTICLE_KEY = "article";
   public final static String FEED_KEY = "feed";
   public final static String TAB_KEY = "Tab";

   private FeedType feedType;

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);

      int position = 0;
      if (savedInstanceState != null)
      {
         FeedType feedType = FeedType.fromInt(savedInstanceState.getInt(FEED_KEY));
         setFeed(feedType);

         // TODO: Open-closed principle?
         switch (feedType)
         {
            case Resources:
               position = 1;
               break;
            default:
               position = 0;
               break;
         }
         loadFeedFromCache(feedType);
      }

      CreateTabs();

      ActionBar actionBar = getSupportActionBar();
      actionBar.setSelectedNavigationItem(position);
   }

   @Override
   public void onSaveInstanceState(Bundle outState)
   {
      if (feedType != null)
      {
         outState.putInt(FEED_KEY, feedType.ordinal());
      }
   }

   private void CreateTabs()
   {
      ActionBar actionBar = getSupportActionBar();
      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

      Tab articlesTab = actionBar.newTab().setText(R.string.articles_text)
            .setTabListener(new FeedTabListener(FeedType.Articles, this));
      actionBar.addTab(articlesTab);

      Tab resourcesTab = actionBar.newTab().setText(R.string.resources_text)
            .setTabListener(new FeedTabListener(FeedType.Resources, this));
      actionBar.addTab(resourcesTab);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      // Inflate the menu; this adds items to the action bar if it is present.
      getSupportMenuInflater().inflate(R.menu.activity_main, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
      switch (item.getItemId())
      {
         case R.id.menu_settings:
            Intent settingsIntent = new Intent(getApplicationContext(), ReaderPreferenceActivity.class);
            startActivity(settingsIntent);
            return true;
         case R.id.menu_refresh:
            loadFeed();
            return true;
      }
      return super.onOptionsItemSelected(item);
   }

   public void setFeed(FeedType feedType)
   {
      this.feedType = feedType;
   }

   @Override
   public void loadFeedFromCache(FeedType feedType)
   {
      setFeed(feedType);
      ArticleList currentList = getCurrentList();
      if (currentList == null || currentList.size() <= 0)
      {
         loadFeed();
      }
      else
      {
         showArticleList(currentList);
      }

   }

   private ArticleList getCurrentList()
   {
      ArticleList result = new ArticleList();
      Uri uri;
      switch (feedType)
      {
         case Resources:
            uri = ArticlesContentProvider.RESOURCES_URI;
            break;
         default:
            uri = ArticlesContentProvider.ARTICLES_URI;
            break;
      }
      Cursor cursor = getContentResolver().query(uri, null, null, null, null);
      OnSaveArticleStrippedDescriptionListener saveArticleStrippedDescriptionListener = new ArticleStrippedDescriptionSaver(
            getContentResolver());
      OnSaveArticleImageListener saveArticleImageListener = new ArticleImageSaver(getContentResolver());
      while (cursor.moveToNext())
      {
         result.add(getArticle(cursor, saveArticleStrippedDescriptionListener, saveArticleImageListener));
      }
      return result;
   }

   private Article getArticle(Cursor cursor,
         OnSaveArticleStrippedDescriptionListener onSaveArticleStrippedDescriptionListener,
         OnSaveArticleImageListener onSaveArticleImageListener)
   {
      String title = cursor.getString(cursor.getColumnIndexOrThrow(ArticlesSQLiteOpenHelper.COLUMN_TITLE));
      String description = cursor.getString(cursor.getColumnIndexOrThrow(ArticlesSQLiteOpenHelper.COLUMN_DESCRIPTION));
      String strippedDescription = cursor.getString(cursor
            .getColumnIndexOrThrow(ArticlesSQLiteOpenHelper.COLUMN_STRIPPED_DESCRIPTION));
      String pubdate = cursor.getString(cursor.getColumnIndexOrThrow(ArticlesSQLiteOpenHelper.COLUMN_PUBDATE));
      String guid = cursor.getString(cursor.getColumnIndexOrThrow(ArticlesSQLiteOpenHelper.COLUMN_GUID));
      String link = cursor.getString(cursor.getColumnIndexOrThrow(ArticlesSQLiteOpenHelper.COLUMN_LINK));
      String imageLocation = cursor.getString(cursor
            .getColumnIndexOrThrow(ArticlesSQLiteOpenHelper.COLUMN_IMAGE_LOCATION));
      Article result = new Article(title, description, strippedDescription, pubdate, guid, link, imageLocation,
            onSaveArticleStrippedDescriptionListener, onSaveArticleImageListener);
      return result;
   }

   public void loadFeed()
   {
      ProgressDialog progressDialog = getProgressDialog();
      String url = getFeedUrl(feedType);
      FeedDownloader downloader = new FeedDownloader(progressDialog, this, getContentResolver(), feedType);
      downloader.execute(url);
   }

   private String getFeedUrl(FeedType feedType)
   {
      Feeds feeds = new Feeds();
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
      if (feedType == FeedType.Articles)
      {
         ArticlesFeed feed = ArticlesFeed.valueOf(prefs.getString(PREFERRED_ARTICLE_FEED, "Full"));
         return feeds.getFeed(feed);
      }
      ResourcesFeed feed = ResourcesFeed.valueOf(prefs.getString(PREFERRED_RESOURCE_FEED, "Full"));
      return feeds.getFeed(feed);
   }

   private ProgressDialog getProgressDialog()
   {
      ProgressDialog result = new ProgressDialog(this);

      result.setMessage("Downloading...");
      result.setIndeterminate(false);
      result.setMax(100);
      result.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

      return result;
   }

   @Override
   public void onFeedDownloaderDone(ArticleList articleList)
   {
      if (articleList == null)
      {
         NoInternetFragment frag = new NoInternetFragment();
         frag.show(getSupportFragmentManager(), null);
      }
      else
      {
         showArticleList(articleList);
      }

   }

   private void showArticleList(ArticleList articleList)
   {
      FragmentManager fm = getSupportFragmentManager();
      ArticleListFragment listFragment = (ArticleListFragment) fm.findFragmentById(R.id.article_list);
      // TODO: Open-closed principle?
      if (listFragment == null)
      {
         listFragment = (ArticleListFragment) fm.findFragmentById(R.id.article_list_single);
      }
      listFragment.setArticles(articleList);

      selectFirstArticleInLandscapeMode();
   }

   private void selectFirstArticleInLandscapeMode()
   {
      FragmentManager fm = getSupportFragmentManager();
      ArticleListFragment listFragment = (ArticleListFragment) fm.findFragmentById(R.id.article_list);
      if (listFragment != null)
      {
         ArticleList currentList = getCurrentList();
         if (currentList != null && currentList.size() > 0)
         {
            setArticle(currentList.get(0));
         }
      }
   }

   @Override
   public void articleSelected(Article article)
   {
      setArticle(article);
   }

   private void setArticle(Article article)
   {
      FragmentManager fm = getSupportFragmentManager();
      ArticleDetailFragment articleDetailFragment = (ArticleDetailFragment) fm.findFragmentById(R.id.article_detail);
      if (articleDetailFragment == null && article != null)
      {
         // Fragment does not exist -> Only article list is visible in this layout
         // Launch new Activity
         Intent intent = new Intent(this, ArticleDetailActivity.class);
         intent.putExtra(ARTICLE_KEY, article);
         startActivity(intent);
      }
      else
      {
         articleDetailFragment.setArticle(article);
      }
   }
}
