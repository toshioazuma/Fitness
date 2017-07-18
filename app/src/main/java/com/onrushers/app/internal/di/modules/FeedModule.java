package com.onrushers.app.internal.di.modules;

import com.onrushers.app.feed.boost.FeedBoostListPresenter;
import com.onrushers.app.feed.boost.impl.FeedBoostListPresenterImpl;
import com.onrushers.app.feed.comment.FeedCommentPresenter;
import com.onrushers.app.feed.comment.impl.FeedCommentPresenterImpl;
import com.onrushers.app.feed.create.FeedCreatePresenter;
import com.onrushers.app.feed.create.impl.FeedCreatePresenterImpl;
import com.onrushers.app.feed.detail.FeedDetailPresenter;
import com.onrushers.app.feed.detail.impl.FeedDetailPresenterImpl;
import com.onrushers.app.file.FileClient;
import com.onrushers.app.file.impl.FileClientImpl;
import com.onrushers.app.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class FeedModule {

	public FeedModule() {

	}

	@Provides
	@PerActivity
	public FeedCreatePresenter provideFeedCreatePresenter(FeedCreatePresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public FeedDetailPresenter provideFeedDetailPresenter(FeedDetailPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public FeedCommentPresenter provideFeedCommentPresenter(FeedCommentPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public FeedBoostListPresenter provideFeedBoostListPresenter(FeedBoostListPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public FileClient provideFileClient(FileClientImpl fileClient) {
		return fileClient;
	}
}
