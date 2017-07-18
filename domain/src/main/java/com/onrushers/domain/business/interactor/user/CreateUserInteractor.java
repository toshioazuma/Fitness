package com.onrushers.domain.business.interactor.user;

import com.onrushers.domain.business.interactor.Interactor;
import com.onrushers.domain.business.type.Gender;

import java.util.Date;

public interface CreateUserInteractor extends Interactor {

	void setFacebookId(String facebookId);

	void setUsername(String username);

	void setFirstName(String firstName);

	void setLastName(String lastName);

	void setEmail(String email);

	void setPassword(String password);

	void setBirthDate(Date birthDate);

	void setGender(Gender gender);

	void setProfilePictureId(Integer profilePictureId);
}
