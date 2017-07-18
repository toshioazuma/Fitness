package com.onrushers.domain.usecases.comment;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.CommentRepository;
import com.onrushers.domain.business.interactor.comment.DeleteCommentInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class DeleteCommentUseCase extends UseCase implements DeleteCommentInteractor {

	private static final String TAG = "DeleteCommentUC";

	private final AuthSessionRepository mAuthSessionRepository;
	private final CommentRepository     mCommentRepository;

	private int mCommentId;

	@Inject
	public DeleteCommentUseCase(AuthSessionRepository authSessionRepository,
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
				.flatMap(new Func1<IAuthSession, Observable<IGenericResult>>() {
					@Override
					public Observable<IGenericResult> call(IAuthSession authSession) {

						return mCommentRepository.deleteComment(mCommentId, authSession.getToken());
					}
				});
	}

	@Override
	public void setComment(IComment comment) {
		mCommentId = comment.getId();
	}
}
