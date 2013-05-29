package org.resilience.resiliencereader.entities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.resilience.resiliencereader.framework.ThreeLineListItem;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

public class Article implements ThreeLineListItem, Parcelable
{
   private String rssTitleElement = "title";
   private String rssDescriptionElement = "description";
   private String rssPubdateElement = "pubDate";
   private String rssGuidElement = "guid";
   private String rssLinkElement = "link";

   public Article(Element element, OnSaveArticleStrippedDescriptionListener onSaveArticleStrippedDescriptionListener,
         OnSaveArticleImageListener onSaveArticleImageListener)
   {
      title = GetStringFromElement(element, rssTitleElement);
      description = GetStringFromElement(element, rssDescriptionElement);

      String pubdateString = GetStringFromElement(element, rssPubdateElement);
      try
      {
         pubdate = parseDate(pubdateString);
      }
      catch (ParseException ex)
      {
         // TODO: Datum niet te parsen. Geen probleem, gewoon open laten
         ex.printStackTrace();
         pubdate = new Date();
      }

      guid = GetStringFromElement(element, rssGuidElement);
      link = GetStringFromElement(element, rssLinkElement);
      this.onSaveArticleStrippedDescriptionListener = onSaveArticleStrippedDescriptionListener;
      this.onSaveArticleImageListener = onSaveArticleImageListener;
   }

   public Article(String title, String description, String strippedDescription, String pubdate, String guid,
         String link, String imageLocation,
         OnSaveArticleStrippedDescriptionListener onSaveArticleStrippedDescriptionListener,
         OnSaveArticleImageListener onSaveArticleImageListener)
   {
      super();
      this.title = title;
      this.description = description;
      this.strippedDescription = strippedDescription;
      try
      {
         this.pubdate = parseDate(pubdate);
      }
      catch (ParseException e)
      {
         // TODO: Datum niet te parsen. Geen probleem, gewoon open laten
         e.printStackTrace();
         this.pubdate = new Date();
      }
      this.guid = guid;
      this.link = link;
      // TODO: Load image from disk, async
      this.onSaveArticleStrippedDescriptionListener = onSaveArticleStrippedDescriptionListener;
      this.onSaveArticleImageListener = onSaveArticleImageListener;
   }

   private Date parseDate(String pubdateString) throws ParseException
   {
      SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.US);
      return sdf.parse(pubdateString);
   }

   private String GetStringFromElement(Element element, String rssElement)
   {
      NodeList childElements = element.getElementsByTagName(rssElement);
      return ((Element) childElements.item(0)).getTextContent();
   }

   private String title;
   private String description;
   private String strippedDescription;
   private Date pubdate;
   private String guid;
   private String link;
   private Drawable image;
   private OnSaveArticleStrippedDescriptionListener onSaveArticleStrippedDescriptionListener;
   private OnSaveArticleImageListener onSaveArticleImageListener;

   public String getTitle()
   {
      return title;
   }

   public String getDescription()
   {
      return description;
   }

   public String getStrippedDescription(boolean forceLoad)
   {
      if (strippedDescription == null && forceLoad)
      {
         // All complete words up to 100 characters
         String result = getDescription().replaceAll("\\<.*?\\>", "").trim();
         result = StringEscapeUtils.unescapeHtml4(result);
         int lastSpace = result.lastIndexOf(" ", 100);
         strippedDescription = result.substring(0, lastSpace);
         saveStrippedDescription();
      }
      return strippedDescription;
   }

   private void saveStrippedDescription()
   {
      if (onSaveArticleStrippedDescriptionListener != null)
      {
         onSaveArticleStrippedDescriptionListener.onSaveArticleStrippedDescription(guid, strippedDescription);
      }
   }

   public Date getPubdate()
   {
      return pubdate;
   }

   @Override
   public String getGuid()
   {
      return guid;
   }

   public String getLink()
   {
      return link;
   }

   @Override
   public String toString()
   {
      if (StringUtils.isNotBlank(title))
      {
         return title;
      }
      return pubdate.toString();
   }

   @Override
   public String getFirstLine()
   {
      return getFirstLine(true);
   }

   @Override
   public String getFirstLine(boolean forceLoad)
   {
      return toString();
   }

   @Override
   public String getSecondLine()
   {
      return getSecondLine(true);
   }

   @Override
   public String getSecondLine(boolean forceLoad)
   {
      return getStrippedDescription(forceLoad);
   }

   @Override
   public String getThirdLine()
   {
      return getThirdLine(true);
   }

   @Override
   public String getThirdLine(boolean forceLoad)
   {
      return DateFormat.format("dd MMMM, kk:mm", pubdate).toString();
   }

   @Override
   public Drawable getDrawable()
   {
      return getDrawable(true);
   }

   @Override
   public Drawable getDrawable(boolean forceLoad)
   {
      return null;
   }

   private URL getImageURL()
   {
      String description = getDescription();
      int indexStart = description.indexOf("<img");
      if (indexStart == -1)
      {
         return null;
      }
      int indexEnd = description.indexOf(">");
      if (indexEnd == -1)
      {
         return null;
      }
      String imgTag = description.substring(indexStart, indexEnd);
      int srcStart = imgTag.indexOf("src=\"") + 5;
      int srcEnd = imgTag.substring(srcStart).indexOf("\"") + srcStart;
      String uriText = imgTag.substring(srcStart, srcEnd);
      try
      {
         return new URL(uriText);
      }
      catch (MalformedURLException e)
      {
         return null;
      }
   }

   @Override
   public Drawable getImage()
   {
      return getImage(true);
   }

   @Override
   public Drawable getImage(boolean forceLoad)
   {
      if (forceLoad && image == null)
      {
         URL imageUrl = getImageURL();
         if (imageUrl != null)
         {
            try
            {
               image = Drawable.createFromStream(imageUrl.openStream(), "src");
               saveImage();
            }
            catch (IOException e)
            {
               // Do nothing
            }
         }
      }
      return image;
   }

   private void saveImage()
   {
      if (onSaveArticleImageListener != null)
      {
         onSaveArticleImageListener.onSaveArticleImage(guid, image);
      }
   }

   @Override
   public long getId()
   {
      return 0;
   }

   @Override
   public int describeContents()
   {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel out, int flags)
   {
      out.writeString(title);
      out.writeString(description);
      out.writeString(pubdate.toString());
      out.writeString(guid);
      out.writeString(link);
   }

   private Article(Parcel in)
   {
      title = in.readString();
      description = in.readString();
      try
      {
         pubdate = parseDate(in.readString());
      }
      catch (ParseException ex)
      {
         // TODO: Datum niet te parsen. Geen probleem, gewoon open laten
         ex.printStackTrace();
         pubdate = new Date();
      }
      guid = in.readString();
      link = in.readString();
   }

   public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>()
      {
         public Article createFromParcel(Parcel in)
         {
            return new Article(in);
         }

         public Article[] newArray(int size)
         {
            return new Article[size];
         }
      };
}
