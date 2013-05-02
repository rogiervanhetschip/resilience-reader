package org.resilience.resiliencereader;

import org.resilience.resiliencereader.entities.FeedType;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

public class MainActivity extends SherlockFragmentActivity {
	
	public final static String TAB_KEY = "Tab";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		if(savedInstanceState != null) {
			// TODO: Tab selecteren
			//FragmentManager fm = getSupportFragmentManager();
			//FeedFragment feedFragment = (FeedFragment) fm.findFragmentById(R.id.feed_fragment);
			//feedFragment.setFeed(feedType);
		} else {
			CreateTabs();
		}
		
	}
	
	@Override
	public void onSaveInstanceState (Bundle outState) {
		//outState.putInt(TAB_KEY, getActionBar().getSelectedNavigationIndex());
	}

	private void CreateTabs() {
		ActionBar actionBar = getSupportActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
	    Tab articlesTab = actionBar.newTab()
	            .setText(R.string.articles_text)
	            .setTabListener(new FeedTabListener(FeedType.Articles));
	    actionBar.addTab(articlesTab);
	    
	    Tab resourcesTab = actionBar.newTab()
	            .setText(R.string.resources_text)
	            .setTabListener(new FeedTabListener(FeedType.Resources));
	    actionBar.addTab(resourcesTab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_settings:
	            Intent settingsIntent = new Intent(this, ReaderPreferenceActivity.class);
	            startActivity(settingsIntent);
	            return true;
			case R.id.menu_refresh:
	            refreshFeed();
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	private void refreshFeed() {
		FragmentManager fm = getSupportFragmentManager();
		FeedFragment feedFragment = (FeedFragment) fm.findFragmentByTag(FeedFragment.FEED_FRAGMENT_TAG);
		feedFragment.loadFeed();
	}
}

