package com.onrushers.app.user.tabs.training.impl;

import com.onrushers.app.user.tabs.training.UserTabTrainingPresenter;
import com.onrushers.app.user.tabs.training.UserTabTrainingView;
import com.onrushers.domain.business.interactor.user.GetUserInteractor;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.common.DefaultSubscriber;

import javax.inject.Inject;

public class UserTabTrainingPresenterImpl implements UserTabTrainingPresenter {

	private final GetUserInteractor mGetUserInteractor;

	private UserTabTrainingView mView;

	@Inject
	public UserTabTrainingPresenterImpl(GetUserInteractor getUserInteractor) {
		mGetUserInteractor = getUserInteractor;
	}

	//region UserTabTrainingPresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void onViewCreated() {
		mGetUserInteractor.execute(new GetUserSubscriber());
	}

	@Override
	public void setView(UserTabTrainingView view) {
		mView = view;
	}

	@Override
	public void setUserId(Integer userId) {
		mGetUserInteractor.setUserId(userId);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	private final class GetUserSubscriber extends DefaultSubscriber<IUser> {

		@Override
		public void onError(Throwable e) {
			mView.showBoostCount(0);
		}

		@Override
		public void onNext(IUser user) {
			mView.showBoostCount(user.getBoostCount());
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
