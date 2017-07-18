package com.onrushers.app.settings.edit_account.impl;

import android.util.Log;

import com.onrushers.app.settings.edit_account.EditAccountPresenter;
import com.onrushers.app.settings.edit_account.EditAccountView;
import com.onrushers.domain.business.interactor.user.GetUserInteractor;
import com.onrushers.domain.business.interactor.user.UpdateUserInfoInteractor;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.Gender;
import com.onrushers.domain.business.type.Unit;
import com.onrushers.domain.common.DefaultSubscriber;

import javax.inject.Inject;

public class EditAccountPresenterImpl implements EditAccountPresenter {

	private static final String TAG = "EditAccountPImpl";

	private final GetUserInteractor        mGetUserInteractor;
	private final UpdateUserInfoInteractor mUpdateUserInfoInteractor;

	private EditAccountView mView;

	@Inject
	public EditAccountPresenterImpl(GetUserInteractor getUserInteractor,
	                                UpdateUserInfoInteractor updateUserInfoInteractor) {
		mGetUserInteractor = getUserInteractor;
		mUpdateUserInfoInteractor = updateUserInfoInteractor;
	}

	//region EditAccountPresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setView(EditAccountView view) {
		mView = view;
	}

	@Override
	public void setUserLastName(String lastName) {
		Log.d(TAG, "update user by changing its last name: " + lastName);
		mUpdateUserInfoInteractor.setLastName(lastName);
		mUpdateUserInfoInteractor.execute(new UpdateUserInfoSubscriber());
	}

	@Override
	public void setUserFirstName(String firstName) {
		Log.d(TAG, "update user by changing its first name: " + firstName);
		mUpdateUserInfoInteractor.setFirstName(firstName);
		mUpdateUserInfoInteractor.execute(new UpdateUserInfoSubscriber());
	}

	@Override
	public void setUserEmail(String email) {
		Log.d(TAG, "update user by changing its email: " + email);
		// mUpdateUserInfoInteractor.setEmail(email);
	}

	@Override
	public void setUserGender(Gender gender) {
		Log.d(TAG, "update user by changing its gender: " + gender);
		mUpdateUserInfoInteractor.setGender(gender);
		mUpdateUserInfoInteractor.execute(new UpdateUserInfoSubscriber());
	}

	@Override
	public void setUserUnit(Unit unit) {
		Log.d(TAG, "update user by changing its unit: " + unit);
	}

	@Override
	public void onViewCreated() {
		mGetUserInteractor.setUserId(null);
		mGetUserInteractor.execute(new GetUserSubcriber());
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	private final class GetUserSubcriber extends DefaultSubscriber<IUser> {

		@Override
		public void onNext(IUser user) {
			if (null == mView) {
				return;
			}

			/**
			 * Pre-fill form with user data in the Interactor
			 */
			mUpdateUserInfoInteractor.setLastName(user.getLastName());
			mUpdateUserInfoInteractor.setFirstName(user.getFirstName());
			mUpdateUserInfoInteractor.setEmail(user.getEmail());
			mUpdateUserInfoInteractor.setGender(user.getGender());

			/**
			 * Notifies View to pre-fill form with user data
			 */
			mView.showUserLastName(user.getLastName());
			mView.showUserFirstName(user.getFirstName());
			mView.showUserLocation("");
			mView.showUserEmail(user.getEmail());
			mView.showUserPhone("");
			mView.showUserWebsite("");

			Gender userGender = user.getGender();
			if (userGender == Gender.Male || userGender == Gender.Female) {
				mView.showUserGender(userGender);
			}
		}
	};

	private final class UpdateUserInfoSubscriber extends DefaultSubscriber<IUser> {

		@Override
		public void onError(Throwable e) {
			Log.e(TAG, "mUpdateUserInfoSubscriber: error: " + e.getLocalizedMessage());
			mView.showUpdateError(e.getLocalizedMessage());
		}

		@Override
		public void onNext(IUser user) {
			Log.d(TAG, "mUpdateUserInfoSubscriber: user: " + user);
		}
	};

	//----------------------------------------------------------------------------------------------
	//endregion
}
