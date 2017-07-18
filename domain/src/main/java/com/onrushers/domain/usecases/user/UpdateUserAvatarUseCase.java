package com.onrushers.domain.usecases.user;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.UserRepository;
import com.onrushers.domain.business.interactor.user.UpdateUserAvatarInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class UpdateUserAvatarUseCase extends UseCase implements UpdateUserAvatarInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final UserRepository        mUserRepository;

	private Integer mProfilePictureId;

	private Integer mUserId = null;
	private String  mToken  = null;

	@Inject
	public UpdateUserAvatarUseCase(AuthSessionRepository authSessionRepository,
	                               UserRepository userRepository,
	                               ThreadExecutor threadExecutor,
	                               PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mUserRepository = userRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		if (mUserId != null && mToken != null) {
			return mUserRepository.updateUserAvatar(mUserId, mProfilePictureId, mToken);
		}

		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IUser>>() {
				@Override
				public Observable<IUser> call(IAuthSession authSession) {

					return mUserRepository.updateUserAvatar(
						authSession.getUserId(), mProfilePictureId, authSession.getToken());
				}
			});
	}

	@Override
	public void setProfilePictureId(Integer profilePictureId) {
		mProfilePictureId = profilePictureId;
	}

	@Override
	public void setUserId(Integer userId) {
		mUserId = userId;
	}

	@Override
	public void setToken(String token) {
		mToken = token;
	}
}
