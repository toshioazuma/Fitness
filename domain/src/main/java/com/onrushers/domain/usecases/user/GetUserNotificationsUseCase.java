package com.onrushers.domain.usecases.user;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.UserRepository;
import com.onrushers.domain.business.interactor.user.GetUserNotificationsInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.PaginationUseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetUserNotificationsUseCase extends PaginationUseCase implements GetUserNotificationsInteractor {

	private final AuthSessionRepository mAuthRepo;
	private final UserRepository        mUserRepo;

	private Integer mUserId;

	@Inject
	public GetUserNotificationsUseCase(AuthSessionRepository authRepo, UserRepository userRepo,
	                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
		super(threadExecutor, postExecutionThread);
		mAuthRepo = authRepo;
		mUserRepo = userRepo;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthRepo.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IPagination>>() {
				@Override
				public Observable<IPagination> call(IAuthSession authSession) {

					final Integer userId;
					if (mUserId != null) {
						userId = mUserId;
					} else {
						userId = authSession.getUserId();
					}

					return mUserRepo.getUserNotifications(userId, getPage(), getPageCount(), authSession.getToken());
				}
			});
	}

	@Override
	public void setUserId(Integer userId) {
		mUserId = userId;
	}
}
