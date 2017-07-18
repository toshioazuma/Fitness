package com.onrushers.domain.usecases.auth_session;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.business.interactor.auth_session.SaveAuthSessionInteractor;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class StoreAuthSessionUseCase extends UseCase implements SaveAuthSessionInteractor {

	private final AuthSessionRepository mAuthSessionRepository;

	private String  mToken;
	private Integer mUserId;

	@Inject
	public StoreAuthSessionUseCase(AuthSessionRepository repository, ThreadExecutor threadExecutor,
	                               PostExecutionThread postExecutionThread) {
		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = repository;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		return mAuthSessionRepository.saveAuthSession(mToken, mUserId);
	}

	@Override
	public void setToken(String token) {
		mToken = token;
	}

	@Override
	public void setUserId(Integer userId) {
		mUserId = userId;
	}
}
