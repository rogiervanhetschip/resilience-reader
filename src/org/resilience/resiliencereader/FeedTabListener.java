package org.resilience.resiliencereader;


import org.resilience.resiliencereader.entities.FeedType;

import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

public class FeedTabListener implements TabListener {
	
	private FeedFragment feedFragment;
	private FeedType feedType;
	
	public FeedTabListener(FeedType feedType) {
		this.feedType = feedType;
	}
	
	public void setFeedFragment(FeedFragment feedFragment) {
		this.feedFragment = feedFragment;
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// Check if the fragment is already initialized
        if (feedFragment == null) {
            // If not, instantiate and add it to the activity
        	feedFragment = FeedFragment.newInstance(feedType);
            ft.add(R.id.feed_fragment_container, feedFragment, FeedFragment.FEED_FRAGMENT_TAG);
        } else {
            // If it exists, simply attach it in order to show it
            ft.attach(feedFragment);
        }
        
		// if(!Is feedFragment al in beeld) {
			//if(feedFragment == null) {
			//	FeedFragment.newInstance(feedType);
			//}
			
			//ft.replace(R.id.feed_fragment_container, feedFragment);
		//}
		
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (feedFragment != null) {
            // Detach the fragment, because another one is being attached
            ft.detach(feedFragment);
        }
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// Do nothing
	}

}
