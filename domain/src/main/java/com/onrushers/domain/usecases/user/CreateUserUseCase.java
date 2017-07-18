package com.onrushers.domain.usecases.user;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.UserRepository;
import com.onrushers.domain.business.interactor.user.CreateUserInteractor;
import com.onrushers.domain.business.model.ICreateUserResult;
import com.onrushers.domain.business.type.Gender;
import com.onrushers.domain.common.DefaultSubscriber;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class CreateUserUseCase extends UseCase implements CreateUserInteractor {

	private final UserRepository        mUserRepository;
	private final AuthSessionRepository mAuthSessionRepository;

	private SimpleDateFormat mDateFormat;

	private String mUsername;
	private String mFirstName;
	private String mLastName;
	private String mEmail;
	private String mPassword;
	private String mBirthDate;
	private Gender mGender;

	private Integer mProfilePictureId;

	private String mFacebookId;


	@Inject
	public CreateUserUseCase(UserRepository repository,
	                         AuthSessionRepository authSessionRepository,
	                         ThreadExecutor executor,
	                         PostExecutionThread thread) {

		super(executor, thread);
		mUserRepository = repository;
		mAuthSessionRepository = authSessionRepository;
		mDateFormat = new SimpleDateFormat();
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return Observable.create(new Observable.OnSubscribe<ICreateUserResult>() {

			@Override
			public void call(final Subscriber<? super ICreateUserResult> subscriber) {

				/**
				 * Create the user through the API
				 */
				mUserRepository.createUser(mUsername, mFirstName, mLastName, mEmail, mPassword,
					mBirthDate, mGender, mFacebookId, mProfilePictureId)
					.subscribe(new DefaultSubscriber<ICreateUserResult>() {

						@Override
						public void onNext(final ICreateUserResult createUserResult) {

							if (createUserResult.isSuccess()) {
								subscriber.onNext(createUserResult);

								/**
								String token = createUserResult.getToken();
								Integer userId = createUserResult.getUserId();

								// Store the created user using the response of the previous API call
								mAuthSessionRepository
									.saveAuthSession(token, userId)
									.subscribe(new DefaultSubscriber<Boolean>() {

										@Override
										public void onNext(Boolean aBoolean) {
											subscriber.onNext(createUserResult);
										}

										@Override
										public void onError(Throwable e) {
											subscriber.onError(e);
										}
									});
								 */

							} else {
								subscriber.onNext(createUserResult);
							}
						}

						@Override
						public void onError(Throwable e) {
							subscriber.onError(e);
						}
					});
			}
		});
	}

	@Override
	public void setFacebookId(String facebookId) {
		mFacebookId = facebookId;
	}

	@Override
	public void setUsername(String username) {
		mUsername = username;
	}

	@Override
	public void setFirstName(String firstName) {
		mFirstName = firstName;
	}

	@Override
	public void setLastName(String lastName) {
		mLastName = lastName;
	}

	@Override
	public void setEmail(String email) {
		mEmail = email;
	}

	@Override
	public void setPassword(String password) {
		mPassword = password;
	}

	@Override
	public void setBirthDate(Date birthDate) {
		mDateFormat.applyPattern("dd-MM-yyyy");
		mBirthDate = mDateFormat.format(birthDate);
	}

	public void setGender(Gender gender) {
		mGender = gender;
	}

	@Override
	public void setProfilePictureId(Integer profilePictureId) {
		mProfilePictureId = profilePictureId;
	}
}
