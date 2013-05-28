package org.resilience.resiliencereader.framework;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ArticlesSQLiteOpenHelper extends SQLiteOpenHelper
{
   public static final String TABLE_ARTICLES = "articles";
   public static final String COLUMN_ID = "_id";
   public static final String COLUMN_TITLE = "title";
   public static final String COLUMN_DESCRIPTION = "description";
   public static final String COLUMN_STRIPPED_DESCRIPTION = "strippeddescription";
   public static final String COLUMN_PUBDATE = "pubdate";
   public static final String COLUMN_GUID = "guid";
   public static final String COLUMN_LINK = "link";
   public static final String COLUMN_IMAGE_LOCATION = "imagelocation";
   public static final String COLUMN_FEED = "feed";

   public static final long FEED_ARTICLE = 1;
   public static final long FEED_RESOURCE = 2;

   private static final String DATABASE_NAME = "articles.db";
   private static final int DATABASE_VERSION = 1;

   public ArticlesSQLiteOpenHelper(Context context)
   {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase db)
   {
      db.execSQL("CREATE TABLE " + TABLE_ARTICLES + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text null, " + COLUMN_DESCRIPTION + " text null, " + COLUMN_STRIPPED_DESCRIPTION
            + " text null, " + COLUMN_PUBDATE + " text null, " + COLUMN_GUID + " text null, " + COLUMN_LINK
            + " text null, " + COLUMN_IMAGE_LOCATION + " text null, " + COLUMN_FEED + " integer not null" + ");");
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
   {
      // Drop table, as it only contains temp data
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
      onCreate(db);
   }

}
