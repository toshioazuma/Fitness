package com.onrushers.domain.usecases.user;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.UserRepository;
import com.onrushers.domain.business.interactor.user.GetUserFansInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetUserFansUseCase extends UseCase implements GetUserFansInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final UserRepository        mUserRepository;

	private Integer mUserId;
	private int mPage      = 1;
	private int mPageCount = 20;

	@Inject
	public GetUserFansUseCase(AuthSessionRepository authSessionRepository,
	                          UserRepository userRepository, ThreadExecutor threadExecutor,
	                          PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mUserRepository = userRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IPagination>>() {
				@Override
				public Observable<IPagination> call(IAuthSession authSession) {

					final Integer userId;
					if (mUserId != null) {
						userId = mUserId;
					} else {
						userId = authSession.getUserId();
					}

					return mUserRepository
						.getUserFans(userId, mPage, mPageCount, authSession.getToken());
				}
			});
	}

	@Override
	public void setUserId(Integer userId) {
		mUserId = userId;
	}

	@Override
	public void setPage(int page) {
		mPage = page;
	}

	@Override
	public void setCount(int count) {
		mPageCount = count;
	}
}
