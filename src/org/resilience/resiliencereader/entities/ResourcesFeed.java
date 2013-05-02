package org.resilience.resiliencereader.entities;

public enum ResourcesFeed {
	Full,
	Energy,
	Economy,
	Environment,
	FoodAndWater,
	Society;
	
	private static final ResourcesFeed[] resourcesFeedValues = ResourcesFeed.values();
	
	public static ResourcesFeed fromInt(int i)
	{
		return resourcesFeedValues[i];
	}
}
