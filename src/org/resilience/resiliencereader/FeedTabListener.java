package org.resilience.resiliencereader;


import org.resilience.resiliencereader.entities.FeedType;

import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

public class FeedTabListener implements TabListener {
	
	private FeedType feedType;
	private FeedTabListenerActivity feedTabListenerActivity;
	
	public FeedTabListener(FeedType feedType, FeedTabListenerActivity feedTabListenerActivity) {
		this.feedType = feedType;
		this.feedTabListenerActivity = feedTabListenerActivity;
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// Load feed in MainActivity
		feedTabListenerActivity.loadFeedFromCache(feedType);
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// Do nothing
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// Do nothing
	}

}
