package org.resilience.resiliencereader.framework;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class ArticlesContentProvider extends ContentProvider
{
   // TODO: Put Resources in separate table
   public final static String AUTHORITY = "org.resilience.resiliencereader.provider";
   public final static String ARTICLES_PATH = "articles";
   public final static int ARTICLES_CODE = 1;
   public final static int ARTICLE_CODE = 2;

   public final static Uri ARTICLES_URI = Uri.parse("content://" + ArticlesContentProvider.AUTHORITY + "/"
         + ArticlesContentProvider.ARTICLES_PATH);

   private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

   private ArticlesSQLiteOpenHelper database;

   static
   {
      uriMatcher.addURI(AUTHORITY, ARTICLES_PATH, ARTICLES_CODE);
      uriMatcher.addURI(AUTHORITY, ARTICLES_PATH + "/#", ARTICLE_CODE);
   }

   @Override
   public int delete(Uri uri, String selection, String[] selectionArgs)
   {
      int uriType = uriMatcher.match(uri);
      SQLiteDatabase db = database.getWritableDatabase();
      int count = 0;
      switch (uriType)
      {
         case ARTICLES_CODE:
            count = db.delete(ArticlesSQLiteOpenHelper.TABLE_ARTICLES, selection, selectionArgs);
            break;
         case ARTICLE_CODE:
            count = db.delete(ArticlesSQLiteOpenHelper.TABLE_ARTICLES, selection, selectionArgs);
            break;
         default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
      }
      getContext().getContentResolver().notifyChange(uri, null);
      return count;
   }

   @Override
   public String getType(Uri uri)
   {
      return null;
   }

   @Override
   public Uri insert(Uri uri, ContentValues values)
   {
      int uriType = uriMatcher.match(uri);
      SQLiteDatabase db = database.getWritableDatabase();
      long id = 0;
      switch (uriType)
      {
         case ARTICLES_CODE:
            id = db.insert(ArticlesSQLiteOpenHelper.TABLE_ARTICLES, null, values);
            break;
         default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
      }
      getContext().getContentResolver().notifyChange(uri, null);

      return Uri.parse(ARTICLES_PATH + id);
   }

   @Override
   public boolean onCreate()
   {
      database = new ArticlesSQLiteOpenHelper(getContext());
      return true;
   }

   @Override
   public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
   {
      SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

      int uriType = uriMatcher.match(uri);
      SQLiteDatabase db = database.getReadableDatabase();
      switch (uriType)
      {
         case ARTICLES_CODE:
            // All of the articles were selected
            break;
         case ARTICLE_CODE:
            // A single article was selected
            queryBuilder.appendWhere(ArticlesSQLiteOpenHelper.COLUMN_ID + "=" + uri.getLastPathSegment());
            break;
         default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
      }

      Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
      cursor.setNotificationUri(getContext().getContentResolver(), uri);

      return cursor;
   }

   @Override
   public int update(Uri uri, ContentValues arg1, String arg2, String[] arg3)
   {
      return 0;
   }
}
