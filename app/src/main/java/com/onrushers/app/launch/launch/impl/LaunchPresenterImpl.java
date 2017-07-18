package com.onrushers.app.launch.launch.impl;

import android.util.Log;

import com.onrushers.app.file.Downloader;
import com.onrushers.app.launch.launch.LaunchPresenter;
import com.onrushers.app.launch.launch.LaunchView;
import com.onrushers.domain.business.interactor.auth_session.GetAuthSessionInteractor;
import com.onrushers.domain.business.interactor.file.DownloadFileInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.common.DefaultSubscriber;

import javax.inject.Inject;

public class LaunchPresenterImpl implements LaunchPresenter {

	private static final String TAG = "LaunchPresenterImpl";

	private final GetAuthSessionInteractor mInteractor;
	private final DownloadFileInteractor   mDownloadFileInteractor;

	private LaunchView mView;

	@Inject
	public LaunchPresenterImpl(GetAuthSessionInteractor interactor, DownloadFileInteractor downloadFileInteractor) {
		mInteractor = interactor;
		mDownloadFileInteractor = downloadFileInteractor;
	}

	@Override
	public void setView(LaunchView view) {
		mView = view;
	}

	@Override
	public void onViewCreated() {
		mInteractor.execute(new LaunchSubscriber());

		mDownloadFileInteractor.setFileId(0);
		mDownloadFileInteractor.execute(new DownloadFileSubscriber());
	}

	@Override
	public void onDestroy() {
		mView = null;
	}

	private final class LaunchSubscriber extends DefaultSubscriber<IAuthSession> {

		@Override
		public void onError(Throwable e) {
			Log.e(TAG, "An error occurs while getting existing auth session: " + e.getLocalizedMessage());
			mView.showLaunchHome();
		}

		@Override
		public void onNext(IAuthSession authSession) {

			if (authSession != null) {
				Downloader.Companion.getInstance().setAccessToken(authSession.getToken());

				Log.i(TAG, "Auth session found, so user is redirected to the home page");
				mView.showLoggedInHome();
			} else {
				Log.i(TAG, "Auth session not found, so user is redirected to the non-logged home page");
				mView.showLaunchHome();
			}
		}
	}

	private final class DownloadFileSubscriber extends DefaultSubscriber<String> {

		@Override public void onNext(String s) {
			Downloader.Companion.getInstance().setTemplate(s);
		}
	}
}
