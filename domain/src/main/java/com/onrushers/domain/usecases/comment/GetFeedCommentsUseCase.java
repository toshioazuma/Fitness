package com.onrushers.domain.usecases.comment;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.CommentRepository;
import com.onrushers.domain.business.interactor.comment.GetFeedCommentsInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetFeedCommentsUseCase extends UseCase implements GetFeedCommentsInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final CommentRepository     mCommentRepository;

	private int mFeedId;
	private int mPage      = 1;
	private int mPageCount = 20;


	@Inject
	public GetFeedCommentsUseCase(AuthSessionRepository authSessionRepository,
	                              CommentRepository commentRepository,
	                              ThreadExecutor threadExecutor,
	                              PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mCommentRepository = commentRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IPagination>>() {
				@Override
				public Observable<IPagination> call(IAuthSession authSession) {

					final Integer loggedUserId = authSession.getUserId();

					return mCommentRepository
						.getFeedComments(mFeedId, mPage, mPageCount, authSession.getToken())
						.map(new Func1<IPagination, IPagination>() {

							@Override
							public IPagination call(IPagination pagination) {

								for (IComment comment : (List<IComment>) pagination.getItems()) {
									comment.compareWithUserId(loggedUserId);
								}
								return pagination;
							}
						});
				}
			});

	}

	@Override
	public void setFeed(IFeed feed) {
		mFeedId = feed.getId();
	}

	@Override
	public void setPage(int page) {
		mPage = page;
	}
}
