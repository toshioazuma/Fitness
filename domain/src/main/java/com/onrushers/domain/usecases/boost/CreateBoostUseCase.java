package com.onrushers.domain.usecases.boost;

import android.util.Log;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.BoostRepository;
import com.onrushers.domain.business.interactor.boost.AddBoostInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class CreateBoostUseCase extends UseCase implements AddBoostInteractor {

	private static final String TAG = "CreateBoostUC";

	private final AuthSessionRepository mAuthSessionRepository;
	private final BoostRepository       mBoostRepository;

	private Integer mNumber;
	private Integer mFeedId;

	@Inject
	public CreateBoostUseCase(AuthSessionRepository authSessionRepository,
	                          BoostRepository boostRepository, ThreadExecutor threadExecutor,
	                          PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mBoostRepository = boostRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IBoost>>() {
				@Override
				public Observable<IBoost> call(IAuthSession authSession) {

					final Integer userId = authSession.getUserId();
					final String accessToken = authSession.getToken();

					Log.d(TAG, "addBoost");
					return mBoostRepository.addBoost(mNumber, mFeedId, userId, accessToken);
				}
			});
	}

	@Override
	public void setNumber(int number) {
		mNumber = number;
	}

	@Override
	public void setFeed(IFeed feed) {
		mFeedId = feed.getId();
	}
}
