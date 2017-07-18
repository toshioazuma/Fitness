package com.onrushers.domain.usecases.user;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.UserRepository;
import com.onrushers.domain.business.interactor.user.UpdateUserCoverInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class UpdateUserCoverUseCase extends UseCase implements UpdateUserCoverInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final UserRepository        mUserRepository;

	private Integer mCoverPictureId;

	@Inject
	public UpdateUserCoverUseCase(AuthSessionRepository authSessionRepository,
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

					return mUserRepository.updateUserCover(
						authSession.getUserId(), mCoverPictureId, authSession.getToken());
				}
			});
	}

	@Override
	public void setCoverPictureId(Integer coverPictureId) {
		mCoverPictureId = coverPictureId;
	}
}
