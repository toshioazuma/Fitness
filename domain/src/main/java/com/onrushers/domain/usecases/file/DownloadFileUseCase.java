package com.onrushers.domain.usecases.file;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.FileRepository;
import com.onrushers.domain.business.interactor.file.DownloadFileInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class DownloadFileUseCase extends UseCase implements DownloadFileInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final FileRepository        mFileRepository;

	private Integer mFileId;

	@Inject
	public DownloadFileUseCase(AuthSessionRepository authSessionRepository,
	                              FileRepository fileRepository, ThreadExecutor threadExecutor,
	                              PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mFileRepository = fileRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<String>>() {
				@Override
				public Observable<String> call(IAuthSession authSession) {

					final String accessToken = authSession.getToken();

					return mFileRepository.getDownloadFile(mFileId, accessToken);
				}
			});
	}

	@Override
	public void setFileId(Integer id) {
		mFileId = id;
	}
}
