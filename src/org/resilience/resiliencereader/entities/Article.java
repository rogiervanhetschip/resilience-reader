package org.resilience.resiliencereader.entities;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.resilience.resiliencereader.framework.TwoLineListItem;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

public class Article implements TwoLineListItem, Parcelable {
	private String rssTitleElement = "title";
	private String rssDescriptionElement = "description";
	private String rssPubdateElement = "pubDate";
	private String rssGuidElement = "guid";
	private String rssLinkElement = "link";
	
	public Article(Element element)
	{
		title = GetStringFromElement(element, rssTitleElement);
		description = GetStringFromElement(element, rssDescriptionElement);
		
		String pubdateString = GetStringFromElement(element, rssPubdateElement);
		try {
			pubdate = parseDate(pubdateString);
		} catch (ParseException ex) {
			// TODO: Datum niet te parsen. Geen probleem, gewoon open laten
			ex.printStackTrace();
			pubdate = new Date();
		}
		
		guid = GetStringFromElement(element, rssGuidElement);
		link = GetStringFromElement(element, rssLinkElement);
	}
	
	private Date parseDate(String pubdateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.US);
		return sdf.parse(pubdateString);
	}

	private String GetStringFromElement(Element element, String rssElement)
	{
		NodeList childElements = element.getElementsByTagName(rssElement);
		return ((Element)childElements.item(0)).getTextContent();
	}
	
	private String title;
	private String description;
	private Date pubdate;
	private String guid;
	private String link;
	
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public Date getPubdate() {
		return pubdate;
	}
	public String getGuid() {
		return guid;
	}
	public String getLink() {
		return link;
	}
	
	@Override
	public String toString() {
		if(StringUtils.isNotBlank(title)) {
			return title;
		}
		return pubdate.toString();
	}

	@Override
	public String getFirstLine() {
		return toString();
	}

	@Override
	public String getSecondLine() {
		return DateFormat.format("dd MMMM yyyy, kk:mm", pubdate).toString();
	}

	@Override
	public long getId() {
		return 0;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(title);
		out.writeString(description);
		out.writeString(pubdate.toString());
		out.writeString(guid);
		out.writeString(link);
	}
	
	private Article(Parcel in) {
		title = in.readString();
		description = in.readString();
		try {
			pubdate = parseDate(in.readString());
		} catch (ParseException ex) {
			// TODO: Datum niet te parsen. Geen probleem, gewoon open laten
			ex.printStackTrace();
			pubdate = new Date();
		}
		guid = in.readString();
		link = in.readString();
	}
	
    public static final Parcelable.Creator<Article> CREATOR
	    = new Parcelable.Creator<Article>() {
		public Article createFromParcel(Parcel in) {
		    return new Article(in);
		}
		
		public Article[] newArray(int size) {
		    return new Article[size];
		}
	};
}
