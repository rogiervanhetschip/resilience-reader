package org.resilience.resiliencereader.entities;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleList extends ArrayList<Article> implements Parcelable
{

   private static final long serialVersionUID = 1L;

   public ArticleList(Document doc, OnSaveArticleStrippedDescriptionListener onSaveArticleStrippedDescriptionListener,
         OnSaveArticleImageListener onSaveArticleImageListener)
   {
      super();
      Element rss = doc.getDocumentElement();
      NodeList channels = rss.getElementsByTagName("channel");
      if (channels.getLength() > 0)
      {
         // TODO: Misschien worden ooit nog meerdere channels toegevoegd?
         NodeList items = ((Element) channels.item(0)).getElementsByTagName("item");
         for (int i = 0; i < items.getLength(); i++)
         {
            Article article = new Article((Element) items.item(i), onSaveArticleStrippedDescriptionListener,
                  onSaveArticleImageListener);
            this.add(article);
         }
      }
   }

   public ArticleList()
   {
      super();
   }

   private ArticleList(Parcel in)
   {
      super();
      while (in.dataAvail() > 0)
      {
         this.add((Article) in.readParcelable(null));
      }
   }

   @Override
   public int describeContents()
   {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel out, int flags)
   {
      for (Article article : this)
      {
         out.writeParcelable(article, flags);
      }
   }

   public static final Parcelable.Creator<ArticleList> CREATOR = new Parcelable.Creator<ArticleList>()
      {
         public ArticleList createFromParcel(Parcel in)
         {
            return new ArticleList(in);
         }

         public ArticleList[] newArray(int size)
         {
            return new ArticleList[size];
         }
      };
}
