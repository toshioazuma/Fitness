package com.onrushers.app.user.tabs.feeds.impl;

import com.onrushers.app.feed.impl.FeedsListPresenterImpl;
import com.onrushers.app.user.tabs.feeds.UserTabFeedsPresenter;
import com.onrushers.app.user.tabs.feeds.UserTabFeedsView;
import com.onrushers.domain.business.interactor.boost.AddBoostInteractor;
import com.onrushers.domain.business.interactor.boost.DeleteBoostInteractor;
import com.onrushers.domain.business.interactor.feed.DeleteFeedInteractor;
import com.onrushers.domain.business.interactor.report.CreateReportFeedInteractor;
import com.onrushers.domain.business.interactor.user.GetUserFeedsInteractor;

import javax.inject.Inject;

public class UserTabFeedsPresenterImpl extends FeedsListPresenterImpl implements UserTabFeedsPresenter {

	private static final String TAG = "UserTabFeedsPRImpl";

	private final GetUserFeedsInteractor mGetUserFeedsInteractor;

	@Inject
	public UserTabFeedsPresenterImpl(GetUserFeedsInteractor getUserFeedsInteractor,
	                                 CreateReportFeedInteractor createReportFeedInteractor,
	                                 DeleteFeedInteractor deleteFeedInteractor,
	                                 AddBoostInteractor addBoostInteractor,
	                                 DeleteBoostInteractor deleteBoostInteractor) {

		super(deleteFeedInteractor, createReportFeedInteractor, addBoostInteractor, deleteBoostInteractor);

		mGetUserFeedsInteractor = getUserFeedsInteractor;
	}

	protected void onGetFeedsPage(int page) {
		mGetUserFeedsInteractor.setPage(page);
		mGetUserFeedsInteractor.execute(new GetFeedsSubscriber());
	}

	//region UserTabFeedsPresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setView(UserTabFeedsView view) {
		super.setView(view);
	}

	@Override
	public void setUserId(Integer userId) {
		mGetUserFeedsInteractor.setUserId(userId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mGetUserFeedsInteractor.unsubscribe();
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
