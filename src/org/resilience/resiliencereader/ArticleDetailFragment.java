package org.resilience.resiliencereader;

import org.resilience.resiliencereader.entities.Article;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

public class ArticleDetailFragment extends SherlockFragment implements OnClickListener {
	
	private Article article;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    // Inflate the layout for this fragment
        return inflater.inflate(R.layout.article_detail, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		Button button = (Button) getView().findViewById(
				R.id.article_detail_browser_button);
		button.setOnClickListener(this);
		
	    if(savedInstanceState != null) {
  			if(savedInstanceState.containsKey(MainActivity.ARTICLE_KEY)) {
  				Article article = savedInstanceState.getParcelable(MainActivity.ARTICLE_KEY);
  				setArticle(article);
  			}
  		}
	}
	
	@Override
	public void onSaveInstanceState (Bundle outState) {
		if(article != null) {
			outState.putParcelable(MainActivity.ARTICLE_KEY, article);
		}
	}
	
	public void setArticle(Article article) {
		this.article = article;
		showArticle();
	}

	private void showArticle() {
		if(article != null)
		{
			WebView webView = (WebView) getView().findViewById(
					R.id.article_detail_webview);
			webView.loadData(article.getDescription(), "text/html", "utf-8");
		}
	}

	@Override
	public void onClick(View view) {
		Intent browserIntent = new Intent();
		browserIntent.setAction(Intent.ACTION_VIEW);
		browserIntent.setData(Uri.parse(article.getLink()));
		getView().getContext().startActivity(browserIntent);
	}
}
