package com.onrushers.app.feed.detail;

import android.view.View;

import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;

import java.util.List;

/**
 * Created by Ludovic on 01/09/16.
 */
public interface FeedDetailView {

	void setOnFeedDetailReadyListener(OnFeedDetailReadyListener readyListener);

	void updateFeed(IFeed feed);

    void showOptions(View anchor);

    void showReportSent();

    void showReportFailed();

    void showComments(List<IComment> comments, int page);

	void showCommentUsers(List<IUser> commentUsers);

	void showCommentSent();

	void showCommentDeleteFailed();

	void showCommentDeleted();

	void showMessage(int messageId);

	void reloadView();

	void dismissView();
}
