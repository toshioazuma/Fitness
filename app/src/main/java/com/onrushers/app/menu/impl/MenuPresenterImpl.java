package com.onrushers.app.menu.impl;

import android.text.TextUtils;

import com.onrushers.app.file.Downloader;
import com.onrushers.app.menu.MenuPresenter;
import com.onrushers.app.menu.MenuView;
import com.onrushers.domain.business.interactor.auth_session.DestroyAuthSessionInteractor;
import com.onrushers.domain.business.interactor.user.GetUserInteractor;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.common.DefaultSubscriber;

import javax.inject.Inject;

public class MenuPresenterImpl implements MenuPresenter {

	private final GetUserInteractor            mGetUserInteractor;
	private final DestroyAuthSessionInteractor mDestroyAuthSessionInteractor;

	private MenuView mView;
	private IUser    mUser;

	@Inject
	public MenuPresenterImpl(GetUserInteractor getUserInteractor,
	                         DestroyAuthSessionInteractor destroyAuthSessionInteractor) {

		mGetUserInteractor = getUserInteractor;
		mDestroyAuthSessionInteractor = destroyAuthSessionInteractor;
	}

	@Override
	public void setView(MenuView view) {
		mView = view;
	}

	@Override
	public void onViewCreated() {
		mGetUserInteractor.execute(new GetUserSubscriber());
	}

	@Override
	public void presentUser(IUser user) {
		if (null == user) {
			return;
		}
		mView.showUserName(user.getUsername());
		mView.showUserGrade(user.getGradeString());

		if (!TextUtils.isEmpty(user.getProfilePicture())) {

			String pictureUrl = Downloader.Companion.getInstance().resourceUrl(user.getProfilePicture());
			mView.showUserAvatar(pictureUrl);
		}
	}

	@Override
	public void representUser() {
		presentUser(mUser);
	}

	@Override
	public void requestInvalidateSession() {
		mDestroyAuthSessionInteractor.execute(new LogOutSubscriber());
	}

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	private final class GetUserSubscriber extends DefaultSubscriber<IUser> {

		@Override
		public void onNext(IUser user) {
			mUser = user;
			presentUser(user);
		}
	}

	class LogOutSubscriber extends DefaultSubscriber<Boolean> {

		@Override
		public void onNext(Boolean aBoolean) {
			mView.showLaunchPage();
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
