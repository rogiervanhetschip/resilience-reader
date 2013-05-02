package org.resilience.resiliencereader;

import org.resilience.resiliencereader.entities.Article;
import org.resilience.resiliencereader.entities.ArticleList;
import org.resilience.resiliencereader.entities.ArticlesFeed;
import org.resilience.resiliencereader.entities.FeedType;
import org.resilience.resiliencereader.entities.Feeds;
import org.resilience.resiliencereader.entities.ResourcesFeed;
import org.resilience.resiliencereader.framework.FeedDownloader;
import org.resilience.resiliencereader.framework.OnFeedDownloaderDoneListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class FeedFragment extends SherlockFragment implements OnFeedDownloaderDoneListener, OnArticleSelectedListener {

	public final static String PREFERENCES_FILENAME = "preferences.xml";
	public final static String PREFERRED_ARTICLE_FEED = "PreferredArticleFeed";
	public final static String PREFERRED_RESOURCE_FEED = "PreferredResourceFeed";
	public final static String ARTICLES_KEY = "articles";
	public final static String ARTICLE_KEY = "article";
	public final static String FEED_KEY = "feed";
	public final static String FEED_FRAGMENT_TAG = "FeedFragment";
	
	private FeedType feedType;
	private ArticleList articleList;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.feed_fragment, container, false);
        
        if(savedInstanceState == null) {
        	if(feedType == null)
        	{
        		setFeed(FeedType.Articles);
        	}
			loadFeed();
		} else {
			FeedType feedType = FeedType.fromInt(savedInstanceState.getInt(FEED_KEY));
			setFeed(feedType);
			
			ArticleList articleList = savedInstanceState.getParcelable(ARTICLES_KEY);
			setArticleList(articleList);
		}
        
        return view;
	}
	
	@Override
	public void onSaveInstanceState (Bundle outState) {
		if(feedType != null) {
			outState.putInt(FEED_KEY, feedType.ordinal());
		}
		
		if(articleList != null) {
			outState.putParcelable(ARTICLES_KEY, articleList);
		}
	}
	
	public void setFeed(FeedType feedType) {
		this.feedType = feedType;
	}
	
	public void loadFeed() {
		ProgressDialog progressDialog = getProgressDialog();
		String url = getFeedUrl(feedType);
		FeedDownloader downloader = new FeedDownloader(progressDialog, this);
		downloader.execute(url);
	}
	
	private String getFeedUrl(FeedType feedType) {
		Feeds feeds = new Feeds();
		SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
		if(feedType == FeedType.Articles)
		{
			ArticlesFeed feed = ArticlesFeed.fromInt(prefs.getInt(PREFERRED_ARTICLE_FEED, 0));
			return feeds.getFeed(feed);
		}
		ResourcesFeed feed = ResourcesFeed.fromInt(prefs.getInt(PREFERRED_RESOURCE_FEED, 0));
		return feeds.getFeed(feed);
	}
	
	private ProgressDialog getProgressDialog() {
		ProgressDialog result = new ProgressDialog(getActivity());
		
		result.setMessage("Downloading...");
		result.setIndeterminate(false);
		result.setMax(100);
		result.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		return result;
	}
	
	@Override
	public void onFeedDownloaderDone(ArticleList articleList) {
		setArticleList(articleList);
	}
	
	private void setArticleList(ArticleList articleList) {
		this.articleList = articleList;
		
		FragmentManager fm = getActivity().getSupportFragmentManager();
		ArticleListFragment listFragment = (ArticleListFragment) fm.findFragmentById(R.id.article_list);
		// TODO: Open-closed principle?
		if(listFragment == null) {
			listFragment = (ArticleListFragment) fm.findFragmentById(R.id.article_list_single);
		}
		listFragment.setArticles(articleList);

		selectFirstArticleInLandscapeMode();
	}
	
	private void selectFirstArticleInLandscapeMode() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		ArticleListFragment listFragment = (ArticleListFragment) fm.findFragmentById(R.id.article_list);
		if(articleList.size() > 0 && listFragment != null)
		{
			setArticle(articleList.get(0));
		}
	}
	
	@Override
	public void articleSelected(Article article) {
		setArticle(article);
	}

	private void setArticle(Article article) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		ArticleDetailFragment articleDetailFragment = (ArticleDetailFragment) fm.findFragmentById(R.id.article_detail);
		if(articleDetailFragment == null && article != null) {
			// Fragment does not exist -> Only article list is visible in this layout
			// Launch new Activity
			Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
		    intent.putExtra(ARTICLE_KEY, article);
		    startActivity(intent);
		}
		else
		{
			articleDetailFragment.setArticle(article);
		}
	}
	
	// Proper design: Any arguments can be required on this method
	public static FeedFragment newInstance(FeedType feedType) {
		FeedFragment result = new FeedFragment();
		result.setFeed(feedType);
		return result;
	}
}
