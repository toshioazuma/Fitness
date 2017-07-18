package com.onrushers.app.explore.tabs.ranking.impl;

import com.onrushers.app.explore.tabs.ranking.ExploreTabRankingPresenter;
import com.onrushers.app.explore.tabs.ranking.ExploreTabRankingView;
import com.onrushers.domain.business.interactor.explore.GetExploreRankInteractor;
import com.onrushers.domain.business.model.IRankPagination;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.List;

import javax.inject.Inject;

public class ExploreTabRankingPresenterImpl implements ExploreTabRankingPresenter {

	private final GetExploreRankInteractor mRankInteractor;

	private ExploreTabRankingView mView;

	private int mPage = 1;

	@Inject
	public ExploreTabRankingPresenterImpl(GetExploreRankInteractor rankInteractor) {
		mRankInteractor = rankInteractor;
	}

	//region ExploreTabRankingPresenter
	//----------------------------------------------------------------------------------------------

	@Override public void setView(ExploreTabRankingView view) {
		mView = view;
	}

	@Override public void onViewCreated() {
		fetchRankUsersAtPage(1);
	}

	@Override public void fetchRankUsersAtPage(int page) {
		mPage = page;
		mRankInteractor.setPage(page);
		mRankInteractor.execute(new GetRankUsersSubscriber());
	}

	@Override public void onDestroyView() {
		mRankInteractor.unsubscribe();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscriber
	//----------------------------------------------------------------------------------------------

	private final class GetRankUsersSubscriber extends DefaultSubscriber<IRankPagination> {

		@Override
		public void onNext(IRankPagination pagination) {

			if (pagination != null && pagination.getCount() > 0) {
				mView.showUsers(pagination.getItems(), mPage);
			} else {
				mView.showNoRanking();
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
