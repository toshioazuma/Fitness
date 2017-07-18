package com.onrushers.domain.usecases.user;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.UserRepository;
import com.onrushers.domain.business.interactor.user.UpdateUserProfileInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class UpdateUserProfileUseCase extends UseCase implements UpdateUserProfileInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final UserRepository        mUserRepository;

	private String mUsername;
	private String mDescription;

	@Inject
	public UpdateUserProfileUseCase(AuthSessionRepository authSessionRepository,
	                                UserRepository userRepository,
	                                ThreadExecutor threadExecutor,
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

					return mUserRepository.updateUserProfile(
						authSession.getUserId(), mUsername, mDescription, authSession.getToken());
				}
			});
	}

	@Override
	public void setUsername(String username) {
		mUsername = username;
	}

	@Override
	public void setDescription(String description) {
		mDescription = description;
	}
}
