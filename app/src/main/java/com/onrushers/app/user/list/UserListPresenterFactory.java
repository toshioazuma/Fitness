package com.onrushers.app.user.list;

import com.onrushers.app.user.list.impl.FanUserListPresenterImpl;
import com.onrushers.app.user.list.impl.HeroUserListPresenterImpl;
import com.onrushers.app.user.list.impl.RushUserListPresenterImpl;
import com.onrushers.domain.business.interactor.boost.GetFeedBoostsInteractor;
import com.onrushers.domain.business.interactor.user.GetUserFansInteractor;
import com.onrushers.domain.business.interactor.user.GetUserHerosInteractor;
import com.onrushers.domain.business.interactor.user.GetUsersInteractor;

public class UserListPresenterFactory {

	private final GetUsersInteractor      mUsersInteractor;
	private final GetUserFansInteractor   mUserFansInteractor;
	private final GetUserHerosInteractor  mUserHerosInteractor;
	private final GetFeedBoostsInteractor mFeedBoostInteractor;

	public UserListPresenterFactory(GetUsersInteractor usersInteractor,
	                                GetUserFansInteractor userFansInteractor,
	                                GetUserHerosInteractor userHerosInteractor,
	                                GetFeedBoostsInteractor feedBoostsInteractor) {

		mUsersInteractor = usersInteractor;
		mUserFansInteractor = userFansInteractor;
		mUserHerosInteractor = userHerosInteractor;
		mFeedBoostInteractor = feedBoostsInteractor;
	}

	public final UserListPresenter getPresenter(UserListConfiguration configuration) {
		UserListPresenter presenter = null;

		switch (configuration.getContextType()) {
			case Fans: {
				FanUserListPresenterImpl presenterImpl = new FanUserListPresenterImpl(
					mUsersInteractor, mUserFansInteractor);
				presenterImpl.setUser(configuration.getWhatUser());
				presenter = presenterImpl;
				break;
			}

			case Heros: {
				HeroUserListPresenterImpl presenterImpl = new HeroUserListPresenterImpl(
					mUsersInteractor, mUserHerosInteractor);
				presenterImpl.setUser(configuration.getWhatUser());
				presenter = presenterImpl;
				break;
			}

			case FeedRushes: {
				RushUserListPresenterImpl presenterImpl = new RushUserListPresenterImpl(
					mUsersInteractor, mFeedBoostInteractor);
				presenterImpl.setFeed(configuration.getWhatFeed());
				presenter = presenterImpl;
				break;
			}
		}

		return presenter;
	}
}
