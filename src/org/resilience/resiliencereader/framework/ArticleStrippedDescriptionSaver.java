package org.resilience.resiliencereader.framework;

import org.resilience.resiliencereader.entities.OnSaveArticleStrippedDescriptionListener;

import android.content.ContentResolver;
import android.content.ContentValues;

// TODO: make this class an AsyncTask?
public class ArticleStrippedDescriptionSaver implements OnSaveArticleStrippedDescriptionListener
{
   private ContentResolver contentResolver;

   public ArticleStrippedDescriptionSaver(ContentResolver contentResolver)
   {
      super();
      this.contentResolver = contentResolver;
   }

   @Override
   public void onSaveArticleStrippedDescription(String guid, String strippedDescription)
   {
      ContentValues values = new ContentValues();
      values.put(ArticlesSQLiteOpenHelper.COLUMN_STRIPPED_DESCRIPTION, strippedDescription);

      // Save strippedDescription to database
      contentResolver.update(ArticlesContentProvider.ARTICLE_OR_RESOURCE_URI, values,
            ArticlesSQLiteOpenHelper.COLUMN_GUID + "= \"" + guid + "\"", null);
   }
}
