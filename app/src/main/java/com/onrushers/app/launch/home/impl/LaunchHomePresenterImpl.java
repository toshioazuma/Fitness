package com.onrushers.app.launch.home.impl;

import com.onrushers.app.launch.home.LaunchHomePresenter;
import com.onrushers.app.launch.home.LaunchHomeView;
import com.onrushers.domain.business.interactor.authentication.LogInInteractor;
import com.onrushers.domain.business.model.ILoginUserResult;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.List;

import javax.inject.Inject;

public class LaunchHomePresenterImpl implements LaunchHomePresenter {

	private static final String TAG = "LaunchHomePrstImpl";

	private LaunchHomeView mView;
	private String         mFacebookId;

	private final LogInInteractor mLogInInteractor;


	@Inject
	public LaunchHomePresenterImpl(LogInInteractor logInInteractor) {
		mLogInInteractor = logInInteractor;
	}

	//region LaunchHomePresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setView(LaunchHomeView view) {
		mView = view;
	}

	@Override
	public void facebookLogin(String facebookId) {
		mFacebookId = facebookId;

		mLogInInteractor.setFacebookId(facebookId);
		mLogInInteractor.execute(new FacebookLogInSubscriber());
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	class FacebookLogInSubscriber extends DefaultSubscriber<ILoginUserResult> {

		@Override
		public void onError(Throwable e) {
			e.printStackTrace();
		}

		@Override
		public void onNext(ILoginUserResult loginUserResult) {

			if (!loginUserResult.isSuccess()) {

				if (loginUserResult.getStatusCode() != null &&
					loginUserResult.getStatusCode().intValue() == 404) {

					mView.showRegistrationPageWithFacebook(mFacebookId);
				} else {
					List<String[]> errorMessages = loginUserResult.getErrorMessages();
					mView.showErrorMessage(errorMessages.get(0));
				}
			} else {
				mView.showHome();
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
