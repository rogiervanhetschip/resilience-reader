package org.resilience.resiliencereader;

import org.resilience.resiliencereader.entities.FeedType;

public interface FeedTabListenerActivity {
	public void loadFeedFromCache(FeedType feedType);
}
