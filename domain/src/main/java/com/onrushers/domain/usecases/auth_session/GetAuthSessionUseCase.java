package com.onrushers.domain.usecases.auth_session;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.business.interactor.auth_session.GetAuthSessionInteractor;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class GetAuthSessionUseCase extends UseCase implements GetAuthSessionInteractor {

	private final AuthSessionRepository mAuthSessionRepository;

	@Inject
	public GetAuthSessionUseCase(AuthSessionRepository repository, ThreadExecutor threadExecutor,
	                             PostExecutionThread postExecutionThread) {
		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = repository;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		return mAuthSessionRepository.getAuthSession(); // hasActiveAuthSession();
	}
}
