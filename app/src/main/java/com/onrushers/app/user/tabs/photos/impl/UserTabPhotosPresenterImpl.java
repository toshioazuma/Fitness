package com.onrushers.app.user.tabs.photos.impl;

import com.onrushers.app.user.tabs.photos.UserTabPhotosPresenter;
import com.onrushers.app.user.tabs.photos.UserTabPhotosView;
import com.onrushers.domain.business.interactor.user.GetUserFeedsInteractor;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IFeedPagination;
import com.onrushers.domain.business.type.FeedType;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.List;

import javax.inject.Inject;

public class UserTabPhotosPresenterImpl implements UserTabPhotosPresenter {

	private final GetUserFeedsInteractor mGetUserFeedsInteractor;

	private UserTabPhotosView mView;
	private int mPage = 1;

	@Inject
	public UserTabPhotosPresenterImpl(GetUserFeedsInteractor getUserFeedsInteractor) {
		mGetUserFeedsInteractor = getUserFeedsInteractor;
		mGetUserFeedsInteractor.setType(FeedType.Post);
	}

	//region UserTabPhotosPresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setView(UserTabPhotosView view) {
		mView = view;
	}

	@Override
	public void setUserId(Integer userId) {
		mGetUserFeedsInteractor.setUserId(userId);
	}

	@Override
	public void onViewCreated() {
		loadUserPhotosFeedsAtPage(1);
	}

	@Override
	public void loadUserPhotosFeedsAtPage(int page) {
		mGetUserFeedsInteractor.setPage(page);
		mGetUserFeedsInteractor.execute(new GetFeedSubscriber());
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	private final class GetFeedSubscriber extends DefaultSubscriber<IFeedPagination> {

		@Override
		public void onNext(IFeedPagination feedPagination) {
			List<IFeed> feedsList = (List<IFeed>) feedPagination.getItems();
			if (feedsList == null || feedsList.isEmpty()) {
				return;
			}

			mView.showFeeds(feedsList, mPage);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
