package com.onrushers.app.user.picker.adapter;

import com.onrushers.domain.business.model.ISearchResult;

public interface UserPickerSelectionListener {

	void onSelectUserResult(ISearchResult userResult);

	void onDeselectUserResult(ISearchResult userResult);
}
