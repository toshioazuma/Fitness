package com.onrushers.app.user.picker.impl;

import android.text.TextUtils;

import com.onrushers.app.user.picker.UserPickerPresenter;
import com.onrushers.app.user.picker.UserPickerView;
import com.onrushers.domain.business.interactor.search.SearchInteractor;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.model.ISearchResult;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.SearchResultType;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public class UserPickerPresenterImpl implements UserPickerPresenter {

	private static final String TAG = "UserPickerPRST";

	private final SearchInteractor mSearchInteractor;

	private UserPickerView     mView;
	private Set<ISearchResult> mSelectedUsers;

	@Inject
	public UserPickerPresenterImpl(SearchInteractor searchInteractor) {
		mSearchInteractor = searchInteractor;
		mSelectedUsers = new HashSet<>();
	}

	//region UserPickerPresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setView(UserPickerView view) {
		mView = view;
	}

	@Override
	public void searchQuery(String query) {
		if (TextUtils.isEmpty(query)) {
			return;
		}
		mSearchInteractor.setType(SearchResultType.User);
		mSearchInteractor.setPage(1);
		mSearchInteractor.setPageCount(50);
		mSearchInteractor.setQuery(query);
		mSearchInteractor.execute(new SearchSubscriber());
	}

	@Override
	public void addSelectedUser(ISearchResult userResult) {
		mSelectedUsers.add(userResult);
	}

	@Override
	public void removeSelectedUser(ISearchResult userResult) {
		mSelectedUsers.remove(userResult);
	}

	@Override
	public ArrayList<IUser> getSelectedUsers() {
		ArrayList selectedUsers = new ArrayList<>();

		if (mSelectedUsers != null) {
			Iterator<ISearchResult> iterator = mSelectedUsers.iterator();
			while (iterator.hasNext()) {
				selectedUsers.add(iterator.next().toUser());
			}
		}

		return selectedUsers;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscriber
	//----------------------------------------------------------------------------------------------

	private final class SearchSubscriber extends DefaultSubscriber<IPagination<ISearchResult>> {

		@Override
		public void onError(Throwable e) {
			super.onError(e);
		}

		@Override
		public void onNext(IPagination<ISearchResult> pagination) {

			List<ISearchResult> usersResult = new ArrayList<>();

			if (pagination.getCount() > 0) {
				Iterator<ISearchResult> iterator = pagination.getItems().iterator();
				while (iterator.hasNext()) {
					ISearchResult resultItem = iterator.next();
					if (SearchResultType.User == resultItem.getType()) {
						usersResult.add(resultItem);
					}
				}
			}

			mView.showUsersList(usersResult);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
