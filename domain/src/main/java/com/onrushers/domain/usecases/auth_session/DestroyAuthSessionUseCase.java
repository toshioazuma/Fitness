package com.onrushers.domain.usecases.auth_session;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.business.interactor.auth_session.DestroyAuthSessionInteractor;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class DestroyAuthSessionUseCase extends UseCase implements DestroyAuthSessionInteractor {

	private final AuthSessionRepository mAuthSessionRepository;

	@Inject
	public DestroyAuthSessionUseCase(AuthSessionRepository authSessionRepository,
	                                 ThreadExecutor threadExecutor,
	                                 PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepository.destroyAuthSession();
	}
}
