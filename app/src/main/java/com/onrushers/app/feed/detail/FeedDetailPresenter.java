package com.onrushers.app.feed.detail;

import com.onrushers.app.feed.detail.adapter.FeedDetailViewType;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IFeed;

/**
 * Created by Ludovic on 01/09/16.
 */
public interface FeedDetailPresenter {

    void setView(FeedDetailView view);

    void setFeed(IFeed feed);

    void reportFeed();

	void deleteFeed();

	FeedDetailViewType getViewType();

    void getCommentsAtPage(int page);

    void addComment(String text);

	void deleteComment(IComment comment);

	void addRush(IFeed feed);

	void removeRush(IFeed feed, IBoost boost);
}
