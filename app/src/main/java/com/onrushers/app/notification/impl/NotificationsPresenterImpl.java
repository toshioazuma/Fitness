package com.onrushers.app.notification.impl;

import com.onrushers.app.notification.NotificationsPresenter;
import com.onrushers.app.notification.NotificationsView;
import com.onrushers.domain.business.interactor.notification.GetNotificationsInteractor;
import com.onrushers.domain.business.interactor.user.GetUserNotificationsInteractor;
import com.onrushers.domain.business.model.INotification;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.common.DefaultSubscriber;

import javax.inject.Inject;

public class NotificationsPresenterImpl implements NotificationsPresenter {

	private final GetUserNotificationsInteractor mInteractor;

	private NotificationsView mView;

	@Inject
	public NotificationsPresenterImpl(GetUserNotificationsInteractor interactor) {
		mInteractor = interactor;
	}

	@Override public void setView(NotificationsView view) {
		mView = view;
	}

	@Override public void onViewCreated() {
		mInteractor.execute(new DefaultSubscriber<IPagination<INotification>>() {

			@Override public void onNext(IPagination<INotification> pagination) {

				if (mView != null) {
					mView.showNotifications(pagination.getItems());
				}
			}
		});
	}

	@Override public void onDestroy() {
		mInteractor.unsubscribe();
	}
}
