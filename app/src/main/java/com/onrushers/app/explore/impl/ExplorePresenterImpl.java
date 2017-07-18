package com.onrushers.app.explore.impl;

import android.text.TextUtils;

import com.onrushers.app.explore.ExplorePresenter;
import com.onrushers.app.explore.ExploreView;
import com.onrushers.domain.business.interactor.search.SearchInteractor;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.model.ISearchResult;
import com.onrushers.domain.business.type.Page;
import com.onrushers.domain.business.type.SearchResultType;
import com.onrushers.domain.common.DefaultSubscriber;

import javax.inject.Inject;

public class ExplorePresenterImpl implements ExplorePresenter {

	private final SearchInteractor mSearchInteractor;

	private static final int COUNT_USERS_PER_PAGE = 50;

	private ExploreView mView;

	@Inject
	public ExplorePresenterImpl(SearchInteractor searchInteractor) {
		mSearchInteractor = searchInteractor;
		mSearchInteractor.setPageCount(COUNT_USERS_PER_PAGE);
	}

	@Override
	public void setView(ExploreView view) {
		mView = view;
	}

	@Override
	public void setQuery(String query) {
		if (TextUtils.isEmpty(query)) {
			return;
		}

		mSearchInteractor.setType(SearchResultType.User);
		mSearchInteractor.setPage(Page.Min.value());
		mSearchInteractor.setQuery(query);

		mSearchInteractor.execute(new DefaultSubscriber<IPagination<ISearchResult>>() {
			@Override
			public void onNext(IPagination<ISearchResult> pagination) {
				mView.showResultUsers(pagination.getItems());
			}
		});
	}

	@Override
	public void onDestroy() {
		mSearchInteractor.unsubscribe();
	}
}
