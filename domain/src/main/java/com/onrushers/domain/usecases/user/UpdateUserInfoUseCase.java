package com.onrushers.domain.usecases.user;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.UserRepository;
import com.onrushers.domain.business.interactor.user.UpdateUserInfoInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.Gender;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class UpdateUserInfoUseCase extends UseCase implements UpdateUserInfoInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final UserRepository        mUserRepository;

	private String mFirstName = "";
	private String mLastName  = "";
	private String mEmail     = "";
	private Gender mGender;

	private String mFacebookId = null;

	@Inject
	public UpdateUserInfoUseCase(AuthSessionRepository authSessionRepository,
	                             UserRepository userRepository, ThreadExecutor threadExecutor,
	                             PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mUserRepository = userRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IUser>>() {
				@Override
				public Observable<IUser> call(IAuthSession authSession) {

					if (mFacebookId != null) {
						return mUserRepository.updateUserFacebookInfo(authSession.getUserId(), mFacebookId, authSession.getToken());
					} else {
						return mUserRepository.updateUserInfo(authSession.getUserId(),
							mFirstName, mLastName, mEmail, mGender, authSession.getToken());
					}
				}
			});
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
	public void setGender(Gender gender) {
		mGender = gender;
	}

	@Override
	public void setFacebookId(String facebookId) {
		mFacebookId = facebookId;
	}
}
