package org.resilience.resiliencereader.framework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.resilience.resiliencereader.entities.Article;
import org.resilience.resiliencereader.entities.ArticleList;
import org.resilience.resiliencereader.entities.FeedType;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

public class FeedDownloader extends AsyncTask<String, Integer, ArticleList>
{

   private ProgressDialog progressDialog;
   private OnFeedDownloaderDoneListener feedDownloaderCallbackReceiver;
   private ContentResolver contentResolver;
   private FeedType feedType;

   public FeedDownloader(ProgressDialog progressDialog, OnFeedDownloaderDoneListener feedDownloaderCallbackReceiver,
         ContentResolver contentResolver, FeedType feedType)
   {
      this.progressDialog = progressDialog;
      this.feedDownloaderCallbackReceiver = feedDownloaderCallbackReceiver;
      this.contentResolver = contentResolver;
      this.feedType = feedType;
   }

   @Override
   protected void onPreExecute()
   {
      if (progressDialog != null)
      {
         progressDialog.show();
      }
   }

   @Override
   protected ArticleList doInBackground(String... urlString)
   {
      ArticleList result = null;
      try
      {
         // TODO: check if we have an internet connection
         URL url = new URL(urlString[0]);
         URLConnection connection = url.openConnection();
         connection.connect();
         // this will be useful so that you can show a typical 0-100% progress bar
         int fileLength = connection.getContentLength();

         // download the file
         InputStream input = connection.getInputStream();
         int streamLength = 0;
         if (fileLength > -1)
         {
            streamLength = fileLength;
         }
         else
         {
            streamLength = 102400; // 100 kilobyte
         }
         ByteArrayOutputStream output = new ByteArrayOutputStream(streamLength);

         // Lezen per kilobyte
         byte data[] = new byte[1024];
         long total = 0;
         int count;
         while ((count = input.read(data)) != -1)
         {
            total += count;
            // publishing the progress....
            publishProgress((int) (total * 100 / streamLength));
            output.write(data, 0, count);
         }
         publishProgress(100);

         input.close();
         output.flush();
         InputSource is = new InputSource(new StringReader(output.toString())); // TODO: Possible risk: Wrong charset

         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(is);

         result = new ArticleList(doc);

         // Clear all existing articles or resources from the database
         // TODO: Add ArticlesContentProvider.RESOURCES_URI, encapsulate database knowledge from this object
         long feedint = ArticlesSQLiteOpenHelper.FEED_ARTICLE;
         if (feedType == FeedType.Resources)
         {
            feedint = ArticlesSQLiteOpenHelper.FEED_RESOURCE;
         }
         contentResolver.delete(ArticlesContentProvider.ARTICLES_URI, ArticlesSQLiteOpenHelper.COLUMN_FEED + "="
               + feedint, null);

         for (Article article : result)
         {
            ContentValues values = getContentValues(article);
            contentResolver.insert(ArticlesContentProvider.ARTICLES_URI, values);
         }
      }
      catch (MalformedURLException ex)
      {
         ex.printStackTrace();
         // TODO: URL is not well formed
      }
      catch (IOException ex)
      {
         // Cannot connect to URL. Solution: Return null.
         ex.printStackTrace();
      }
      catch (SAXException ex)
      {
         ex.printStackTrace();
         // TODO: DocumentBuilder could not be made
      }
      catch (ParserConfigurationException ex)
      {
         // TODO: XML could not be parsed
         ex.printStackTrace();
      }
      return result;
   }

   // TODO: Put this in a separate class, it is not FeedDownloader's responsibility
   private ContentValues getContentValues(Article article)
   {
      ContentValues result = new ContentValues(7);
      result.put(ArticlesSQLiteOpenHelper.COLUMN_TITLE, article.getTitle());
      result.put(ArticlesSQLiteOpenHelper.COLUMN_DESCRIPTION, article.getDescription());
      result.put(ArticlesSQLiteOpenHelper.COLUMN_STRIPPED_DESCRIPTION, article.getStrippedDescription(false));
      result.put(ArticlesSQLiteOpenHelper.COLUMN_PUBDATE, article.getPubdate().toString());
      result.put(ArticlesSQLiteOpenHelper.COLUMN_GUID, article.getGuid());
      result.put(ArticlesSQLiteOpenHelper.COLUMN_LINK, article.getLink());

      Drawable image = article.getImage(false);
      if (image != null)
      {
         String imageLocation = ""; // TODO
         result.put(ArticlesSQLiteOpenHelper.COLUMN_IMAGE_LOCATION, imageLocation);
      }

      if (article.getClass().getName() == "Resource")
      {
         result.put(ArticlesSQLiteOpenHelper.COLUMN_FEED, ArticlesSQLiteOpenHelper.FEED_RESOURCE);
      }
      else
      {
         result.put(ArticlesSQLiteOpenHelper.COLUMN_FEED, ArticlesSQLiteOpenHelper.FEED_ARTICLE);
      }
      return result;
   }

   @Override
   protected void onProgressUpdate(Integer... progress)
   {
      super.onProgressUpdate(progress);
      progressDialog.setProgress(progress[0]);
   }

   @Override
   protected void onPostExecute(ArticleList result)
   {
      feedDownloaderCallbackReceiver.onFeedDownloaderDone(result);
      progressDialog.hide();
   }

}
