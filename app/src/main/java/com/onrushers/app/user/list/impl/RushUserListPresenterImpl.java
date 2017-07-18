package com.onrushers.app.user.list.impl;

import com.onrushers.app.R;
import com.onrushers.app.user.list.UserListView;
import com.onrushers.app.user.list.interfaces.RushUserListPresenter;
import com.onrushers.domain.business.interactor.boost.GetFeedBoostsInteractor;
import com.onrushers.domain.business.interactor.user.GetUsersInteractor;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class RushUserListPresenterImpl extends UserListPresenterImpl implements RushUserListPresenter {

	private final GetFeedBoostsInteractor mFeedBoostInteractor;

	public RushUserListPresenterImpl(GetUsersInteractor usersInteractor,
	                                 GetFeedBoostsInteractor feedBoostsInteractor) {
		super(usersInteractor);
		mFeedBoostInteractor = feedBoostsInteractor;
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
		mFeedBoostInteractor.setPage(page);
		mFeedBoostInteractor.execute(new GetFeedBoostsSubscriber());
	}

	@Override
	public void fetchNextContextListPage() {
		super.fetchNextContextListPage();
		getView().showLoading();
		mFeedBoostInteractor.setPage(getPage());
		mFeedBoostInteractor.execute(new GetFeedBoostsSubscriber());
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region RushUserListPresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setFeed(IFeed feed) {
		mFeedBoostInteractor.setFeed(feed);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscriber
	//----------------------------------------------------------------------------------------------

	private final class GetFeedBoostsSubscriber extends DefaultSubscriber<IPagination<IBoost>> {

		@Override
		public void onNext(final IPagination<IBoost> pagination) {

			Observable.from(pagination.getItems())
				.map(new Func1<IBoost, Integer>() {
					@Override
					public Integer call(IBoost boost) {
						return boost.getUserId();
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

						if (userIds.isEmpty()) {
							getView().hideLoading();
							return;
						}

						getUsers(userIds)
							.subscribe(new DefaultSubscriber<List<IUser>>() {

								@Override
								public void onNext(List<IUser> users) {
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
