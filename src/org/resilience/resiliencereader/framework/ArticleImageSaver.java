package org.resilience.resiliencereader.framework;

import org.resilience.resiliencereader.entities.OnSaveArticleImageListener;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.drawable.Drawable;

// TODO: make this class an AsyncTask?
public class ArticleImageSaver implements OnSaveArticleImageListener
{
   private ContentResolver contentResolver;

   public ArticleImageSaver(ContentResolver contentResolver)
   {
      super();
      this.contentResolver = contentResolver;
   }

   @Override
   public void onSaveArticleImage(String guid, Drawable image)
   {
      // TODO: Save image to disk. Async?
      String locationImage = "";

      ContentValues values = new ContentValues();
      values.put(ArticlesSQLiteOpenHelper.COLUMN_IMAGE_LOCATION, locationImage);

      // Update the image location to database
      contentResolver.update(ArticlesContentProvider.ARTICLE_OR_RESOURCE_URI, values,
            ArticlesSQLiteOpenHelper.COLUMN_GUID + "= \"" + guid + "\"", null);
   }
}
