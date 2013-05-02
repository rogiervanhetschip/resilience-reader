package org.resilience.resiliencereader.entities;

public enum FeedType {
	Articles,
	Resources;
	
	private static final FeedType[] feedTypeValues = FeedType.values();
	
	public static FeedType fromInt(int i)
	{
		return feedTypeValues[i];
	}
}
