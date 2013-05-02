package org.resilience.resiliencereader.entities;

import java.util.HashMap;

public class Feeds {
	private HashMap<ArticlesFeed, String> articlesFeeds;
	private HashMap<ResourcesFeed, String> resourcesFeeds;
	
	public Feeds()
	{
		articlesFeeds = new HashMap<ArticlesFeed, String>();
		articlesFeeds.put(ArticlesFeed.Full, "http://www.resilience.org/articles.xml");
		articlesFeeds.put(ArticlesFeed.Energy, "http://www.resilience.org/articles-energy.xml");
		articlesFeeds.put(ArticlesFeed.Environment, "http://www.resilience.org/articles-environment.xml");
		articlesFeeds.put(ArticlesFeed.FoodAndWater, "http://www.resilience.org/articles-food-water.xml");
		articlesFeeds.put(ArticlesFeed.Society, "http://www.resilience.org/articles-society.xml");
		
		resourcesFeeds = new HashMap<ResourcesFeed, String>();
		resourcesFeeds.put(ResourcesFeed.Full, "http://www.resilience.org/resources.xml");
		resourcesFeeds.put(ResourcesFeed.Energy, "http://www.resilience.org/resources-energy.xml");
		resourcesFeeds.put(ResourcesFeed.Environment, "http://www.resilience.org/resources-environment.xml");
		resourcesFeeds.put(ResourcesFeed.FoodAndWater, "http://www.resilience.org/resources-food-water.xml");
		resourcesFeeds.put(ResourcesFeed.Society, "http://www.resilience.org/resources-society.xml");
	}
	
	public String getFeed(ArticlesFeed feed)
	{
		if(articlesFeeds.containsKey(feed)){
			return articlesFeeds.get(feed);
		}
		return "";
	}
	
	public String getFeed(ResourcesFeed feed)
	{
		if(resourcesFeeds.containsKey(feed)){
			return resourcesFeeds.get(feed);
		}
		return "";
	}
}
