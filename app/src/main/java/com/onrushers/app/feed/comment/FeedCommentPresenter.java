package com.onrushers.app.feed.comment;

import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IFeed;

public interface FeedCommentPresenter {

	void setView(FeedCommentView view);

	void setFeed(IFeed feed);

	void loadComments();

	void addComment(String comment);

	void deleteComment(IComment comment);
}
