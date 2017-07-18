package com.onrushers.app.launch.signin.impl;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;

import com.onrushers.app.R;
import com.onrushers.app.internal.di.PerActivity;
import com.onrushers.app.launch.signin.SignInPresenter;
import com.onrushers.app.launch.signin.SignInView;
import com.onrushers.domain.business.interactor.file.UploadFileInteractor;
import com.onrushers.domain.business.interactor.search.SearchUserInteractor;
import com.onrushers.domain.business.interactor.user.UpdateUserAvatarInteractor;
import com.onrushers.domain.business.model.ICreateUserResult;
import com.onrushers.domain.business.model.IUploadResult;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.Gender;
import com.onrushers.domain.common.DefaultSubscriber;
import com.onrushers.domain.usecases.user.CreateUserUseCase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

@PerActivity
public class SignInPresenterImpl implements SignInPresenter {

	private static final String TAG = "SignInPresenterImpl";

	private final UploadFileInteractor       mUploadFileInteractor;
	private final SearchUserInteractor       mSearchUserInteractor;
	private final CreateUserUseCase          mCreateUserUseCase;
	private final UpdateUserAvatarInteractor mUpdateAvatarInteractor;
	private final SimpleDateFormat           mDateFormat;

	private SignInView mView;
	private Date       mBirthDate;
	private File       mFileToPost;

	@Inject
	public SignInPresenterImpl(UploadFileInteractor uploadFileInteractor,
	                           SearchUserInteractor searchUserInteractor,
	                           CreateUserUseCase createUserUseCase,
	                           UpdateUserAvatarInteractor updateAvatarInteractor) {

		mUploadFileInteractor = uploadFileInteractor;
		mSearchUserInteractor = searchUserInteractor;
		mCreateUserUseCase = createUserUseCase;
		mUpdateAvatarInteractor = updateAvatarInteractor;

		mDateFormat = new SimpleDateFormat();
		mDateFormat.applyPattern("dd/MM/yyyy");
	}

	@Override
	public void setView(SignInView view) {
		mView = view;
	}

	@Override
	public void onDestroy() {
		if (mCreateUserUseCase != null) {
			mCreateUserUseCase.unsubscribe();
		}
		mView = null;
	}

	@Override
	public void setFacebookId(String facebookId) {
		mCreateUserUseCase.setFacebookId(facebookId);
	}

	@Override
	public void setUsername(String username) {
		mCreateUserUseCase.setUsername(username);
	}

	@Override
	public void setLastName(String lastName) {
		mCreateUserUseCase.setLastName(lastName);
	}

	@Override
	public void setFirstName(String firstName) {
		mCreateUserUseCase.setFirstName(firstName);
	}

	@Override
	public void setPassword(String password) {
		mCreateUserUseCase.setPassword(password);
	}

	@Override
	public void setEmail(String email) {
		mCreateUserUseCase.setEmail(email);
	}

	@Override
	public void selectBirthDate(Date birthDate) {
		mBirthDate = birthDate;
		mCreateUserUseCase.setBirthDate(birthDate);
		mView.showBirthDate(mDateFormat.format(birthDate));
	}

	@Override
	public void selectFemaleGender() {
		mCreateUserUseCase.setGender(Gender.Female);
	}

	@Override
	public void selectMaleGender() {
		mCreateUserUseCase.setGender(Gender.Male);
	}

	@Override
	public void selectMetricUnit() {

	}

	@Override
	public void selectImperialUnit() {

	}

	@Override
	public SpannableString getTermsSpan(Context context) {
		String fullString = context.getString(R.string.sign_in_messages_terms);
		String termsKeyword = context.getString(R.string.sign_in_messages_terms_terms_word);

		SpannableString span = new SpannableString(fullString);

		int start = fullString.indexOf(termsKeyword);
		int end = start + termsKeyword.length();

		span.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.blue)),
			start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		span.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		return span;
	}

	@Override
	public boolean isBirthDateValid() {
		if (mBirthDate == null) {
			return false;
		}

		return mBirthDate.before(Calendar.getInstance().getTime());
	}

	@Override
	public void postPicture(File file) {
		mFileToPost = file;
	}

	@Override
	public void removePicture() {
		mCreateUserUseCase.setProfilePictureId(null);
	}

	@Override
	public void searchUser(String username) {
		mSearchUserInteractor.setSlug(username);
		mSearchUserInteractor.execute(new SearchUserSubscriber());
	}

	@Override
	public void createUser() {
		mView.showLoading();
		mCreateUserUseCase.execute(new CreateUserSubscriber());
	}

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	private final class FileUploadSubscriber extends DefaultSubscriber<IUploadResult> {

		FileUploadSubscriber(Integer userId, String token) {
			mUpdateAvatarInteractor.setUserId(userId);
			mUpdateAvatarInteractor.setToken(token);
		}

		@Override
		public void onNext(IUploadResult uploadResult) {
			if (uploadResult.isSuccess()) {
				/** mCreateUserUseCase.setProfilePictureId(uploadResult.getId()); */
				mUpdateAvatarInteractor.setProfilePictureId(uploadResult.getId());
				mUpdateAvatarInteractor.execute(new UpdateUserAvatarSubscriber());
			} else {
				/** handle upload error */
			}
		}
	}

	private final class SearchUserSubscriber extends DefaultSubscriber<IUser> {

		@Override
		public void onError(Throwable e) {
			super.onError(e);
		}

		@Override
		public void onNext(IUser iUser) {
			Log.d(TAG, "SearchUserSubscriber: " + iUser);
		}
	}

	private final class CreateUserSubscriber extends DefaultSubscriber<ICreateUserResult> {

		@Override
		public void onError(Throwable e) {
			mView.hideLoading();

			Log.e(TAG, "An error occurs on user sign-in: " + e.getLocalizedMessage());
			mView.showSignInErrorMessage(e.getLocalizedMessage());
		}

		@Override
		public void onNext(ICreateUserResult result) {
			mView.hideLoading();

			if (result.isSuccess()) {

				if (mFileToPost != null) {
					mUploadFileInteractor.setFile(mFileToPost, "image/jpeg");
					mUploadFileInteractor.setToken(result.getToken());
					mUploadFileInteractor.execute(new FileUploadSubscriber(result.getUserId(), result.getToken()));
				}

				Log.i(TAG, "User sign-in did succeed");
				mView.onSuccessfulSignIn();

			} else {

				List<String[]> errorMessages = result.getErrorMessages();
				if (!errorMessages.isEmpty()) {
					/**
					 * Show the first error message.
					 */
					String[] messages = errorMessages.get(0);
					String lastMessage = messages[messages.length - 1];
					mView.showSignInErrorMessage(lastMessage);

					Log.e(TAG, "An error occurs on user sign-in: " + lastMessage);
				} else {
					Log.e(TAG, "An unknown error on user sign-in occurs");
				}
			}
		}
	}

	private final class UpdateUserAvatarSubscriber extends DefaultSubscriber<IUser> {

		@Override
		public void onNext(IUser user) {
			/** does nothing */
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
