package com.onrushers.domain.business.interactor.user;

import com.onrushers.domain.business.interactor.Interactor;
import com.onrushers.domain.business.type.Gender;

public interface UpdateUserInfoInteractor extends Interactor {

	void setFirstName(String firstName);

	void setLastName(String lastName);

	void setEmail(String email);

	void setGender(Gender gender);

	void setFacebookId(String facebookId);
}
