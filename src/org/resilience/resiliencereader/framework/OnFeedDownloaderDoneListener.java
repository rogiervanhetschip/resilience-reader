package org.resilience.resiliencereader.framework;

import org.resilience.resiliencereader.entities.ArticleList;

public interface OnFeedDownloaderDoneListener {
	public void onFeedDownloaderDone(ArticleList articleList);
}
