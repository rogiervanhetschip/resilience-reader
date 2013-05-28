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
   public final static String RESOURCES_PATH = "resources";
   public final static int ARTICLES_CODE = 1;
   public final static int ARTICLE_CODE = 2;
   public final static int RESOURCES_CODE = 3;
   public final static int RESOURCE_CODE = 4;

   public final static Uri ARTICLES_URI = Uri.parse("content://" + ArticlesContentProvider.AUTHORITY + "/"
         + ArticlesContentProvider.ARTICLES_PATH);
   public final static Uri RESOURCES_URI = Uri.parse("content://" + ArticlesContentProvider.AUTHORITY + "/"
         + ArticlesContentProvider.RESOURCES_PATH);

   private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

   private ArticlesSQLiteOpenHelper database;

   static
   {
      uriMatcher.addURI(AUTHORITY, ARTICLES_PATH, ARTICLES_CODE);
      uriMatcher.addURI(AUTHORITY, ARTICLES_PATH + "/#", ARTICLE_CODE);
      uriMatcher.addURI(AUTHORITY, RESOURCES_PATH, RESOURCES_CODE);
      uriMatcher.addURI(AUTHORITY, RESOURCES_PATH + "/#", RESOURCE_CODE);
   }

   @Override
   public int delete(Uri uri, String selection, String[] selectionArgs)
   {
      int uriType = uriMatcher.match(uri);
      SQLiteDatabase db = database.getWritableDatabase();
      int count = 0;
      String lastPathSegment = uri.getLastPathSegment();
      switch (uriType)
      {
         case ARTICLES_CODE:
            count = db.delete(ArticlesSQLiteOpenHelper.TABLE_ARTICLES, ArticlesSQLiteOpenHelper.COLUMN_FEED + "="
                  + ArticlesSQLiteOpenHelper.FEED_ARTICLE + ((!empty(selection)) ? " AND (" + selection + ')' : ""),
                  selectionArgs);
            break;
         case ARTICLE_CODE:
            count = db.delete(ArticlesSQLiteOpenHelper.TABLE_ARTICLES, ArticlesSQLiteOpenHelper.COLUMN_ID + "="
                  + lastPathSegment + " AND " + ArticlesSQLiteOpenHelper.COLUMN_FEED + "="
                  + ArticlesSQLiteOpenHelper.FEED_ARTICLE + ((!empty(selection)) ? " AND (" + selection + ')' : ""),
                  selectionArgs);
            break;
         case RESOURCES_CODE:
            count = db.delete(ArticlesSQLiteOpenHelper.TABLE_ARTICLES, ArticlesSQLiteOpenHelper.COLUMN_FEED + "="
                  + ArticlesSQLiteOpenHelper.FEED_RESOURCE + ((!empty(selection)) ? " AND (" + selection + ')' : ""),
                  selectionArgs);
            break;
         case RESOURCE_CODE:
            count = db.delete(ArticlesSQLiteOpenHelper.TABLE_ARTICLES, ArticlesSQLiteOpenHelper.COLUMN_ID + "="
                  + lastPathSegment + " AND " + ArticlesSQLiteOpenHelper.COLUMN_FEED + "="
                  + ArticlesSQLiteOpenHelper.FEED_RESOURCE + ((!empty(selection)) ? " AND (" + selection + ')' : ""),
                  selectionArgs);
            break;
         default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
      }
      getContext().getContentResolver().notifyChange(uri, null);
      return count;
   }

   private static boolean empty(String selection)
   {
      return selection == null || selection.trim().length() <= 0;
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
            values.put(ArticlesSQLiteOpenHelper.COLUMN_FEED, ArticlesSQLiteOpenHelper.FEED_ARTICLE);
            id = db.insert(ArticlesSQLiteOpenHelper.TABLE_ARTICLES, null, values);
            break;
         case RESOURCES_CODE:
            values.put(ArticlesSQLiteOpenHelper.COLUMN_FEED, ArticlesSQLiteOpenHelper.FEED_RESOURCE);
            id = db.insert(ArticlesSQLiteOpenHelper.TABLE_ARTICLES, null, values);
            break;
         default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
      }
      getContext().getContentResolver().notifyChange(uri, null);

      String lastPathSegment = uri.getLastPathSegment();
      return Uri.parse(ARTICLES_PATH + "/" + lastPathSegment + "/" + id);
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
            queryBuilder
                  .appendWhere(ArticlesSQLiteOpenHelper.COLUMN_FEED + "=" + ArticlesSQLiteOpenHelper.FEED_ARTICLE);
            break;
         case ARTICLE_CODE:
            // A single article was selected
            queryBuilder.appendWhere(ArticlesSQLiteOpenHelper.COLUMN_ID + "=" + uri.getLastPathSegment());
            queryBuilder
                  .appendWhere(ArticlesSQLiteOpenHelper.COLUMN_FEED + "=" + ArticlesSQLiteOpenHelper.FEED_ARTICLE);
            break;
         case RESOURCES_CODE:
            // All of the resources were selected
            queryBuilder.appendWhere(ArticlesSQLiteOpenHelper.COLUMN_FEED + "="
                  + ArticlesSQLiteOpenHelper.FEED_RESOURCE);
            break;
         case RESOURCE_CODE:
            // A single resource was selected
            queryBuilder.appendWhere(ArticlesSQLiteOpenHelper.COLUMN_FEED + "="
                  + ArticlesSQLiteOpenHelper.FEED_RESOURCE);
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
