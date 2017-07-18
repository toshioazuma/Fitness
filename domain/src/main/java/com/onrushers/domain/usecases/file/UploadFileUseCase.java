package com.onrushers.domain.usecases.file;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.FileRepository;
import com.onrushers.domain.business.interactor.file.UploadFileInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IUploadResult;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class UploadFileUseCase extends UseCase implements UploadFileInteractor {

	private final FileRepository        mRepository;
	private final AuthSessionRepository mAuthSessionRepository;

	private String mMimeType;
	private File   mFile;

	private String mToken = null;

	@Inject
	public UploadFileUseCase(FileRepository repository, AuthSessionRepository authSessionRepository,
	                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
		super(threadExecutor, postExecutionThread);
		mRepository = repository;
		mAuthSessionRepository = authSessionRepository;
	}

	@Override
	public void setFile(File file, String mimeType) {
		mFile = file;
		mMimeType = mimeType;
	}

	@Override
	public void setToken(String token) {
		mToken = token;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		if (mToken != null) {
			return mRepository.uploadFile(mMimeType, mFile, mToken);
		}

		return mAuthSessionRepository.getAuthSession()
				.flatMap(new Func1<IAuthSession, Observable<IUploadResult>>() {
					@Override
					public Observable<IUploadResult> call(IAuthSession authSession) {

						final String accessToken = authSession.getToken();

						return mRepository.uploadFile(mMimeType, mFile, accessToken);
					}
				});
	}
}
