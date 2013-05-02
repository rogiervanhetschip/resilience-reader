package org.resilience.resiliencereader;

import org.resilience.resiliencereader.entities.Article;
import org.resilience.resiliencereader.entities.ArticleList;
import org.resilience.resiliencereader.framework.TwoLineAdapter;

import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ArticleListFragment extends SherlockFragment implements OnItemClickListener {

	private ArticleList articleList;
	private OnArticleSelectedListener listItemClickCallback;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.article_list, container, false);
	}

	public void setArticles(ArticleList articleList) {
		this.articleList = articleList;
		showArticles();
	}
	
	private void showArticles() {
		ListView listview = (ListView) getView().findViewById(
				R.id.article_list_view);
		TwoLineAdapter adapter = new TwoLineAdapter(articleList);
		listview.setAdapter(adapter);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		FragmentManager fm = ((FragmentActivity)activity).getSupportFragmentManager();
		Fragment feedFragment = fm.findFragmentByTag(FeedFragment.FEED_FRAGMENT_TAG);
		
		// This makes sure that the container fragment has implemented
		// the callback interface. If not, it throws an exception.
		try {
			listItemClickCallback = (OnArticleSelectedListener) feedFragment;
		} catch (ClassCastException e) {
			throw new ClassCastException(feedFragment.toString()
					+ " must implement OnArticleSelectedListener");
		}
	}
	
	@Override
	public void onViewCreated (View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		ListView listview = (ListView) getView().findViewById(
				R.id.article_list_view);
		listview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view,
			int position, long id) {
		
		// Opzoeken welk artikel is aangeklikt
		Article article = articleList.get(position);
		listItemClickCallback.articleSelected(article);
	}
}
