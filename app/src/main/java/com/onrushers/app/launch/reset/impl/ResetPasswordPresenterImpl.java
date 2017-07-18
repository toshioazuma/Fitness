package com.onrushers.app.launch.reset.impl;

import com.onrushers.app.launch.reset.ResetPasswordPresenter;
import com.onrushers.app.launch.reset.ResetPasswordView;
import com.onrushers.domain.business.interactor.reset.ResetPasswordInteractor;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.common.DefaultSubscriber;

import javax.inject.Inject;

public class ResetPasswordPresenterImpl implements ResetPasswordPresenter {

	private final ResetPasswordInteractor mResetPasswordInteractor;

	private ResetPasswordView mView;
	private String            mUsername;


	@Inject
	public ResetPasswordPresenterImpl(ResetPasswordInteractor resetPasswordInteractor) {
		mResetPasswordInteractor = resetPasswordInteractor;
	}

	@Override
	public void setView(ResetPasswordView view) {
		mView = view;
	}

	@Override
	public void requestResetPassword(String username) {
		if (username == null || username.length() == 0) {
			return;
		}

		mUsername = username;
		mResetPasswordInteractor.setUsername(username);
		mResetPasswordInteractor.execute(new ResetSubscriber());
	}

	private final class ResetSubscriber extends DefaultSubscriber<IGenericResult> {

		@Override
		public void onNext(IGenericResult iGenericResult) {

			if (iGenericResult.isSuccess()) {

				boolean isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(mUsername).matches();
				mView.showEmailSentPage(mUsername, isEmail);

			} else {

				mView.showUserNotFoundError();
			}
		}
	}
}
