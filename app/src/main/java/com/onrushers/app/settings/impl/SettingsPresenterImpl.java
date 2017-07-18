package com.onrushers.app.settings.impl;

import com.onrushers.app.settings.SettingsPresenter;
import com.onrushers.app.settings.SettingsView;
import com.onrushers.domain.business.interactor.auth_session.GetAuthSessionInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.common.DefaultSubscriber;

import javax.inject.Inject;

public class SettingsPresenterImpl implements SettingsPresenter {

	private SettingsView mView;

	@Inject
	GetAuthSessionInteractor mAuthInteractor;

	@Inject
	public SettingsPresenterImpl() {

	}

	@Override
	public void setView(SettingsView view) {
		mView = view;
	}

	@Override
	public void onViewCreated() {
		mAuthInteractor.execute(new DefaultSubscriber<IAuthSession>() {
			@Override
			public void onNext(IAuthSession authSession) {
				mView.showUserId(authSession.getUserId());
			}
		});
	}
}
