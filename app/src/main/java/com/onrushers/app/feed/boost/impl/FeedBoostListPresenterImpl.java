package com.onrushers.app.feed.boost.impl;

import com.onrushers.app.feed.boost.FeedBoostListPresenter;
import com.onrushers.app.feed.boost.FeedBoostListView;
import com.onrushers.domain.business.interactor.boost.GetFeedBoostsInteractor;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IBoostPagination;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.List;

import javax.inject.Inject;

public class FeedBoostListPresenterImpl implements FeedBoostListPresenter {

	private FeedBoostListView mView;

	private int mBoostsPage = 1;

	private final GetFeedBoostsInteractor mGetFeedBoostsInteractor;


	@Inject
	public FeedBoostListPresenterImpl(GetFeedBoostsInteractor getFeedBoostsInteractor) {

		mGetFeedBoostsInteractor = getFeedBoostsInteractor;
	}

	//region FeedBoostListPresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setView(FeedBoostListView view) {
		mView = view;
	}

	@Override
	public void setFeed(IFeed feed) {
		mGetFeedBoostsInteractor.setFeed(feed);
	}

	@Override
	public void loadBoosts() {
		mGetFeedBoostsInteractor.execute(new GetBoostsSubscriber());
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	class GetBoostsSubscriber extends DefaultSubscriber<IBoostPagination> {

		@Override
		public void onError(Throwable e) {
			e.printStackTrace();
		}

		@Override
		public void onNext(IBoostPagination iBoostPagination) {
			mView.showBoosts((List<IBoost>) iBoostPagination.getItems(), mBoostsPage);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
