package com.onrushers.app.user.list.impl;

import com.onrushers.app.R;
import com.onrushers.app.user.list.UserListView;
import com.onrushers.app.user.list.interfaces.RelationUserListPresenter;
import com.onrushers.domain.business.interactor.user.GetUserFansInteractor;
import com.onrushers.domain.business.interactor.user.GetUsersInteractor;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.model.IRelation;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class FanUserListPresenterImpl extends UserListPresenterImpl implements RelationUserListPresenter {

	private final GetUserFansInteractor mGetFansInteractor;

	public FanUserListPresenterImpl(GetUsersInteractor usersInteractor,
	                                GetUserFansInteractor fansInteractor) {
		super(usersInteractor);
		mGetFansInteractor = fansInteractor;
	}

	//region UserListPresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setView(UserListView view) {
		super.setView(view);
		view.setTitle(R.string.fans_title);
	}

	@Override
	public void fetchContextListAtPage(int page) {
		getView().showLoading();
		mGetFansInteractor.setPage(page);
		mGetFansInteractor.execute(new GetRelationsListSubscriber());
	}

	@Override
	public void fetchNextContextListPage() {
		super.fetchNextContextListPage();
		getView().showLoading();
		mGetFansInteractor.setPage(getPage());
		mGetFansInteractor.execute(new GetRelationsListSubscriber());
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region RelationUserListPresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setUser(IUser user) {
		mGetFansInteractor.setUserId(user.getId());
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscriber
	//----------------------------------------------------------------------------------------------

	private final class GetRelationsListSubscriber extends DefaultSubscriber<IPagination<IRelation>> {

		@Override
		public void onNext(IPagination<IRelation> pagination) {
			
			Observable.from(pagination.getItems())
				.map(new Func1<IRelation, Integer>() {
					@Override
					public Integer call(IRelation relation) {
						return relation.getFanId();
					}
				})
				.subscribe(new DefaultSubscriber<Integer>() {
					final Set<Integer> userIds = new HashSet<>();

					@Override
					public void onNext(Integer id) {
						userIds.add(id);
					}

					@Override
					public void onCompleted() {

						getUsers(userIds)
							.subscribe(new Action1<List<IUser>>() {
								@Override
								public void call(List<IUser> users) {
									getView().showUsers(users, getPage());
									getView().hideLoading();
								}
							});
					}
				});
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
