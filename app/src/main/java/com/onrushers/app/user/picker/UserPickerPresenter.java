package com.onrushers.app.user.picker;

import com.onrushers.domain.business.model.ISearchResult;
import com.onrushers.domain.business.model.IUser;

import java.util.ArrayList;

public interface UserPickerPresenter {

	void setView(UserPickerView view);

	void searchQuery(String query);

	void addSelectedUser(ISearchResult userResult);

	void removeSelectedUser(ISearchResult userResult);

	ArrayList<IUser> getSelectedUsers();
}
