package com.onrushers.app.feed.comment.impl;

import com.onrushers.app.feed.comment.FeedCommentPresenter;
import com.onrushers.app.feed.comment.FeedCommentView;
import com.onrushers.domain.business.interactor.comment.CreateCommentInteractor;
import com.onrushers.domain.business.interactor.comment.DeleteCommentInteractor;
import com.onrushers.domain.business.interactor.comment.GetFeedCommentsInteractor;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.ICommentPagination;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.List;

import javax.inject.Inject;

public class FeedCommentPresenterImpl implements FeedCommentPresenter {

	private static final String TAG = "FeedCommentPrstImpl";

	private FeedCommentView mView;

	private int commentsPage = 1;

	private final GetFeedCommentsInteractor mGetFeedCommentsInteractor;
	private final CreateCommentInteractor   mCreateCommentInteractor;
	private final DeleteCommentInteractor   mDeleteCommentInteractor;

	@Inject
	public FeedCommentPresenterImpl(GetFeedCommentsInteractor getFeedCommentsInteractor,
	                                CreateCommentInteractor createCommentInteractor,
	                                DeleteCommentInteractor deleteCommentInteractor) {

		mGetFeedCommentsInteractor = getFeedCommentsInteractor;
		mCreateCommentInteractor = createCommentInteractor;
		mDeleteCommentInteractor = deleteCommentInteractor;
	}

	//region FeedCommentPresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setView(FeedCommentView view) {
		mView = view;
	}

	@Override
	public void setFeed(IFeed feed) {
		mGetFeedCommentsInteractor.setFeed(feed);
		mCreateCommentInteractor.setFeed(feed);
	}

	@Override
	public void loadComments() {
		mGetFeedCommentsInteractor.execute(new GetCommentsSubscriber());
	}

	@Override
	public void addComment(String comment) {
		mView.showCreateCommentLoading();
		mCreateCommentInteractor.setText(comment);
		mCreateCommentInteractor.execute(new CreateCommentSubscriber());
	}

	@Override
	public void deleteComment(IComment comment) {
		mDeleteCommentInteractor.setComment(comment);
		mDeleteCommentInteractor.execute(new DeleteCommentSubscriber());
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	class GetCommentsSubscriber extends DefaultSubscriber<ICommentPagination> {

		@Override
		public void onError(Throwable e) {
			e.printStackTrace();
		}

		@Override
		public void onNext(ICommentPagination iCommentPagination) {
			mView.showComments((List<IComment>) iCommentPagination.getItems(), commentsPage);
		}
	}

	class CreateCommentSubscriber extends DefaultSubscriber<IComment> {

		@Override
		public void onError(Throwable e) {
			e.printStackTrace();
			mView.hideCreateCommentLoading();
		}

		@Override
		public void onNext(IComment iComment) {
			mView.hideCreateCommentLoading();
			loadComments();
		}
	}

	class DeleteCommentSubscriber extends DefaultSubscriber<IGenericResult> {

		@Override
		public void onError(Throwable e) {
			e.printStackTrace();
		}

		@Override
		public void onNext(IGenericResult iGenericResult) {
			if (iGenericResult.isSuccess()) {
				loadComments();
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
