package com.onrushers.domain.usecases.feed;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.FeedRepository;
import com.onrushers.domain.business.interactor.feed.DeleteFeedInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class DeleteFeedUseCase extends UseCase implements DeleteFeedInteractor {

	private static final String TAG = "DeleteFeedUC";

	private final AuthSessionRepository mAuthSessionRepository;
	private final FeedRepository        mFeedRepository;

	private Integer mFeedId;

	@Inject
	public DeleteFeedUseCase(AuthSessionRepository authSessionRepository,
	                         FeedRepository feedRepository,
	                         ThreadExecutor threadExecutor,
	                         PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mFeedRepository = feedRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IGenericResult>>() {
				@Override
				public Observable<IGenericResult> call(IAuthSession authSession) {

					return mFeedRepository.deleteFeed(mFeedId, authSession.getToken());
				}
			});
	}

	@Override
	public void setFeed(IFeed feed) {
		mFeedId = feed.getId();
	}
}
