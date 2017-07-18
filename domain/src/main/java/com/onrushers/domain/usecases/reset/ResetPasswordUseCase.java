package com.onrushers.domain.usecases.reset;

import com.onrushers.domain.boundaries.ResetPasswordRepository;
import com.onrushers.domain.business.interactor.reset.ResetPasswordInteractor;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class ResetPasswordUseCase extends UseCase implements ResetPasswordInteractor {

	private final ResetPasswordRepository mResetPasswordRepository;

	private String mUsername;


	@Inject
	public ResetPasswordUseCase(ResetPasswordRepository resetPasswordRepository,
	                               ThreadExecutor threadExecutor,
	                               PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mResetPasswordRepository = resetPasswordRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mResetPasswordRepository.resetPassword(mUsername);
	}

	@Override
	public void setUsername(String username) {
		mUsername = username;
	}
}
