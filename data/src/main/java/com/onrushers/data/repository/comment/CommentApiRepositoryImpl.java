package com.onrushers.data.repository.comment;

import com.onrushers.data.api.service.CommentService;
import com.onrushers.data.models.Comment;
import com.onrushers.domain.boundaries.CommentRepository;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.business.model.IPagination;

import javax.inject.Inject;

import rx.Observable;

public class CommentApiRepositoryImpl implements CommentRepository {

	private final CommentService mCommentService;

	@Inject
	public CommentApiRepositoryImpl(CommentService commentService) {
		mCommentService = commentService;
	}

	@Override
	public Observable<IComment> createComment(
			String text, int feedId, int userId, String accessToken) {

		final Comment comment = new Comment();
		comment.text = text;
		comment.feedId = feedId;
		comment.userId = userId;

		return mCommentService.postComment(comment, accessToken).cast(IComment.class);
	}

	@Override
	public Observable<IGenericResult> deleteComment(
			int commentId, String accessToken) {

		return mCommentService
				.deleteComment(commentId, accessToken)
				.cast(IGenericResult.class);
	}

	@Override
	public Observable<IPagination> getFeedComments(
			int feedId, int page, int count, String accessToken) {

		return mCommentService
				.getFeedComments(feedId, page, count, accessToken)
				.cast(IPagination.class);
	}
}
