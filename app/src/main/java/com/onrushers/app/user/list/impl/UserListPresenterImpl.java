package com.onrushers.app.user.list.impl;

import com.onrushers.app.user.list.UserListPresenter;
import com.onrushers.app.user.list.UserListView;
import com.onrushers.domain.business.interactor.user.GetUsersInteractor;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class UserListPresenterImpl implements UserListPresenter {

	private final GetUsersInteractor mUsersInteractor;

	private UserListView mView;
	private int mPage = 1;

	@Inject
	public UserListPresenterImpl(GetUsersInteractor usersInteractor) {
		mUsersInteractor = usersInteractor;
	}

	@Override
	public void setView(UserListView view) {
		mView = view;
	}

	@Override
	public void fetchContextListAtPage(int page) {
		throw new RuntimeException("Stub!");
	}

	@Override
	public void fetchNextContextListPage() {
		++mPage;
	}

	protected UserListView getView() {
		return mView;
	}

	protected int getPage() {
		return mPage;
	}

	protected Observable<List<IUser>> getUsers(final Set<Integer> usersIds) {
		mUsersInteractor.setUserIds(new ArrayList<>(usersIds));

		return Observable.create(new Observable.OnSubscribe<List<IUser>>() {
			@Override
			public void call(final Subscriber<? super List<IUser>> subscriber) {
				mUsersInteractor.execute(new DefaultSubscriber<List<IUser>>() {

					@Override
					public void onNext(List<IUser> users) {
						subscriber.onNext(users);
					}
				});
			}
		});
	}
}
