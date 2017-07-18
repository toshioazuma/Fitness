package com.onrushers.domain.usecases.other;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.OtherRepository;
import com.onrushers.domain.business.interactor.other.GetSliderInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.ISliderResult;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetSliderUseCase extends UseCase implements GetSliderInteractor {

	private final AuthSessionRepository mAuthRepo;
	private final OtherRepository       mOtherRepo;

	@Inject
	public GetSliderUseCase(AuthSessionRepository authRepo, OtherRepository otherRepo,
	                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
		super(threadExecutor, postExecutionThread);
		mAuthRepo = authRepo;
		mOtherRepo = otherRepo;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthRepo.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<ISliderResult>>() {
				@Override
				public Observable<ISliderResult> call(IAuthSession authSession) {

					String accessToken = authSession.getToken();

					return mOtherRepo.getSlider(accessToken);
				}
			});
	}
}
