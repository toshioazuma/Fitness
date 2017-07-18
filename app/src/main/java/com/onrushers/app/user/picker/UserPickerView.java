package com.onrushers.app.user.picker;

import com.onrushers.domain.business.model.ISearchResult;
import com.onrushers.domain.business.model.IUser;

import java.util.ArrayList;
import java.util.List;

public interface UserPickerView {

	void showUsersList(List<ISearchResult> usersResultList);

	ArrayList<IUser> getSelectedUsers();
}
