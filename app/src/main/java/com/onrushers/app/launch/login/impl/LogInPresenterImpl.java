package com.onrushers.app.launch.login.impl;

import android.util.Log;

import com.onrushers.app.launch.login.LogInPresenter;
import com.onrushers.app.launch.login.LoginView;
import com.onrushers.domain.business.interactor.authentication.LogInInteractor;
import com.onrushers.domain.business.model.ILoginUserResult;
import com.onrushers.domain.business.model.LoginUserResult;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.List;

import javax.inject.Inject;

public class LogInPresenterImpl implements LogInPresenter {

	private static final String TAG = "LogInPresenterImpl";

	private final LogInInteractor mLogInInteractor;

	private LoginView mView;

	@Inject
	public LogInPresenterImpl(LogInInteractor interactor) {
		mLogInInteractor = interactor;
	}

	@Override
	public void setView(LoginView view) {
		mView = view;
	}

	@Override
	public void onDestroy() {
		if (mLogInInteractor != null) {
			mLogInInteractor.unsubscribe();
		}
		mView = null;
	}

	@Override
	public void setUsername(String username) {
		mLogInInteractor.setUsername(username);
	}

	@Override
	public void setPassword(String password) {
		mLogInInteractor.setPassword(password);
	}

	@Override
	public void logIn() {
		mView.showLoading();
		mLogInInteractor.execute(new LogInSubscriber());
	}

	private final class LogInSubscriber extends DefaultSubscriber<ILoginUserResult> {

		@Override
		public void onError(Throwable e) {
			mView.hideLoading();

			Log.e(TAG, "An error occurs on user sign-in: " + e.getLocalizedMessage());
			mView.showLogInErrorMessage(e.getLocalizedMessage());
			e.printStackTrace();
		}

		@Override
		public void onNext(ILoginUserResult result) {
			mView.hideLoading();

			if (result.isSuccess()) {
				Log.i(TAG, "User log-in did succeed");
				mView.onSuccessfulLogIn();

			} else {

				List<String[]> errorMessages = result.getErrorMessages();
				if (!errorMessages.isEmpty()) {
					/**
					 * Show the first error message.
					 */
					String[] messages = errorMessages.get(0);
					String lastMessage = messages[messages.length - 1];
					mView.showLogInErrorMessage(lastMessage);

					Log.e(TAG, "An error occurs on user log-in: " + lastMessage);
				} else {
					Log.e(TAG, "An unknown error on user log-in occurs");
				}
			}
		}
	}
}
