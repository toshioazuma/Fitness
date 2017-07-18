package com.onrushers.app.feed.comment;

import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IFeed;

import java.util.List;

public interface FeedCommentView {

	void setFeed(IFeed feed);

	void showComments(List<IComment> comments, int page);

	void showCreateCommentLoading();

	void hideCreateCommentLoading();
}
