package com.onrushers.app.launch.signin;

import android.content.Context;
import android.text.SpannableString;

import java.io.File;
import java.util.Date;

public interface SignInPresenter {

	void setView(SignInView view);

	void onDestroy();

	void setFacebookId(String facebookId);

	void setUsername(String username);

	void setLastName(String lastName);

	void setFirstName(String firstName);

	void setPassword(String password);

	void setEmail(String email);

	void selectBirthDate(Date birthDate);

	void selectFemaleGender();

	void selectMaleGender();

	void selectMetricUnit();

	void selectImperialUnit();

	SpannableString getTermsSpan(Context context);

	boolean isBirthDateValid();

	void postPicture(File file);

	void removePicture();

	void searchUser(String username);

	void createUser();

}
