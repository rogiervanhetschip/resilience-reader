package org.resilience.resiliencereader.entities;

public enum ArticlesFeed {
	Full,
	Energy,
	Economy,
	Environment,
	FoodAndWater,
	Society;
	
	private static final ArticlesFeed[] articlesFeedValues = ArticlesFeed.values();
	
	public static ArticlesFeed fromInt(int i)
	{
		return articlesFeedValues[i];
	}
}
