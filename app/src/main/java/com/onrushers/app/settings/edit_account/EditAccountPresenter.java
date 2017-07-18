package com.onrushers.app.settings.edit_account;

import com.onrushers.domain.business.type.Gender;
import com.onrushers.domain.business.type.Unit;

public interface EditAccountPresenter {

	void onViewCreated();

	void setView(EditAccountView view);

	void setUserLastName(String lastName);

	void setUserFirstName(String firstName);

	void setUserEmail(String email);

	void setUserGender(Gender gender);

	void setUserUnit(Unit unit);
}
