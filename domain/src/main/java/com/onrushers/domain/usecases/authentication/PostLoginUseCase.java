package com.onrushers.domain.usecases.authentication;

import com.onrushers.domain.boundaries.AuthenticationRepository;
import com.onrushers.domain.business.interactor.authentication.LogInInteractor;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class PostLoginUseCase extends UseCase implements LogInInteractor {

	private final AuthenticationRepository mAuthRepository;

	private String mUsername;
	private String mPassword;
	private String mFacebookId;

	@Inject
	public PostLoginUseCase(AuthenticationRepository repository, ThreadExecutor threadExecutor,
	                           PostExecutionThread postExecutionThread) {
		super(threadExecutor, postExecutionThread);
		mAuthRepository = repository;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		if (mFacebookId != null) {
			return mAuthRepository.loginFacebookUser(mFacebookId);
		} else {
			return mAuthRepository.loginUser(mUsername, mPassword);
		}
	}

	@Override
	public void setUsername(String username) {
		mUsername = username;
	}

	@Override
	public void setPassword(String password) {
		mPassword = password;
	}

	@Override
	public void setFacebookId(String facebookId) {
		mFacebookId = facebookId;
	}
}
