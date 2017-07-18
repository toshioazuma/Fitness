package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.business.model.IPagination;

import rx.Observable;

public interface CommentRepository {

	Observable<IComment> createComment(
			String text, int feedId, int userId, String accessToken);

	Observable<IGenericResult> deleteComment(
			int commentId, String accessToken);

	Observable<IPagination> getFeedComments(
			int feedId, int page, int count, String accessToken);
}
