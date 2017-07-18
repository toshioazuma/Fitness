package com.onrushers.domain.usecases.boost;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.BoostRepository;
import com.onrushers.domain.business.interactor.boost.DeleteBoostInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class DeleteBoostUseCase extends UseCase implements DeleteBoostInteractor {

	private static final String TAG = "DeleteBoostUC";

	private final AuthSessionRepository mAuthSessionRepository;
	private final BoostRepository       mBoostRepository;

	private Integer mBoostId;


	@Inject
	public DeleteBoostUseCase(AuthSessionRepository authSessionRepository,
	                          BoostRepository boostRepository,
	                          ThreadExecutor threadExecutor,
	                          PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mBoostRepository = boostRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IGenericResult>>() {
				@Override
				public Observable<IGenericResult> call(IAuthSession authSession) {

					return mBoostRepository
						.deleteBoost(mBoostId, authSession.getToken());
				}
			});
	}

	@Override
	public void setBoost(IBoost boost) {
		mBoostId = boost.getId();
	}
}
