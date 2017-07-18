package com.onrushers.domain.usecases.user;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.UserRepository;
import com.onrushers.domain.business.interactor.user.GetUserInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetUserUseCase extends UseCase implements GetUserInteractor {

	private static final String TAG = "GetUserUseCase";

	private final UserRepository        mUserRepository;
	private final AuthSessionRepository mAuthSessionRepository;

	private Integer mUserId;

	@Inject
	public GetUserUseCase(UserRepository userRepository, AuthSessionRepository authSessionRepository,
	                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
		super(threadExecutor, postExecutionThread);
		mUserRepository = userRepository;
		mAuthSessionRepository = authSessionRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		return mAuthSessionRepository.getAuthSession()
				.flatMap(new Func1<IAuthSession, Observable<IUser>>() {
					@Override
					public Observable<IUser> call(IAuthSession authSession) {

						final Integer userId;
						if (mUserId != null) {
							userId = mUserId;
						} else {
							userId = authSession.getUserId();
						}

						return mUserRepository.getUser(userId, authSession.getToken());
					}
				});
	}

	@Override
	public void setUserId(Integer userId) {
		mUserId = userId;
	}
}
