package com.onrushers.app.settings.edit_account;

import com.onrushers.domain.business.type.Gender;
import com.onrushers.domain.business.type.Unit;

public interface EditAccountView {

	void showUserLastName(String lastName);

	void showUserFirstName(String firstName);

	void showUserLocation(String location);

	void showUserEmail(String email);

	void showUserPhone(String phone);

	void showUserWebsite(String website);

	void showUserGender(Gender gender);

	void showUserUnit(Unit unit);

	void showUpdateError(String message);
}
