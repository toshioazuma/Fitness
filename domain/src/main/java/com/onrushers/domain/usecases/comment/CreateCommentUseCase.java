package com.onrushers.domain.usecases.comment;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.CommentRepository;
import com.onrushers.domain.business.interactor.comment.CreateCommentInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class CreateCommentUseCase extends UseCase implements CreateCommentInteractor {

	private static final String TAG = "CreateCommentUC";

	private final AuthSessionRepository mAuthSessionRepository;
	private final CommentRepository     mCommentRepository;

	private Integer mFeedId;
	private String  mText;

	@Inject
	public CreateCommentUseCase(AuthSessionRepository authSessionRepository,
	                            CommentRepository commentRepository, ThreadExecutor threadExecutor,
	                            PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mCommentRepository = commentRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepository.getAuthSession()
				.flatMap(new Func1<IAuthSession, Observable<IComment>>() {
					@Override
					public Observable<IComment> call(IAuthSession authSession) {

						final Integer userId = authSession.getUserId();
						final String accessToken = authSession.getToken();

						return mCommentRepository.createComment(mText, mFeedId, userId, accessToken);
					}
				});
	}

	@Override
	public void setFeed(IFeed feed) {
		mFeedId = feed.getId();
	}

	@Override
	public void setText(String text) {
		mText = text;
	}
}
