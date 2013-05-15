package org.resilience.resiliencereader;

import org.resilience.resiliencereader.entities.Article;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class ArticleDetailActivity extends SherlockFragmentActivity implements OnArticleSelectedListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_article_detail);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		setArticle((Article) intent.getParcelableExtra(MainActivity.ARTICLE_KEY));
	}
	
	// TODO: Bij roteren, naar andere activity switchen
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
	            // This is called when the Home (Up) button is pressed
	            // in the Action Bar.
	            //Intent parentActivityIntent = new Intent(this, MainActivity.class);
	            //parentActivityIntent.addFlags(
	            //        Intent.FLAG_ACTIVITY_CLEAR_TOP |
	            //        Intent.FLAG_ACTIVITY_NEW_TASK);
	            //startActivity(parentActivityIntent);
	            finish();
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	private void setArticle(Article article) {
		FragmentManager fm = getSupportFragmentManager();
		ArticleDetailFragment fragment = (ArticleDetailFragment) fm.findFragmentById(R.id.article_detail_single);
		fragment.setArticle(article);
	}

	@Override
	public void articleSelected(Article article) {
		setArticle(article);
		
	}
	
	// TODO: Up button?
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		setArticle(null);
	}
}
