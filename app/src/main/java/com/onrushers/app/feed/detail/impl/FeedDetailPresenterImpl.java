package com.onrushers.app.feed.detail.impl;

import com.onrushers.app.R;
import com.onrushers.app.common.bus.events.DeleteFeedEvent;
import com.onrushers.app.feed.detail.FeedDetailPresenter;
import com.onrushers.app.feed.detail.FeedDetailView;
import com.onrushers.app.feed.detail.adapter.FeedDetailViewType;
import com.onrushers.app.feed.detail.adapter.viewtypes.FeedDetailViewAvatarChange;
import com.onrushers.app.feed.detail.adapter.viewtypes.FeedDetailViewPost;
import com.onrushers.app.feed.detail.adapter.viewtypes.FeedDetailViewRegister;
import com.onrushers.domain.bus.BusProvider;
import com.onrushers.domain.business.interactor.auth_session.GetAuthSessionInteractor;
import com.onrushers.domain.business.interactor.boost.AddBoostInteractor;
import com.onrushers.domain.business.interactor.boost.DeleteBoostInteractor;
import com.onrushers.domain.business.interactor.comment.CreateCommentInteractor;
import com.onrushers.domain.business.interactor.comment.DeleteCommentInteractor;
import com.onrushers.domain.business.interactor.comment.GetFeedCommentsInteractor;
import com.onrushers.domain.business.interactor.feed.DeleteFeedInteractor;
import com.onrushers.domain.business.interactor.feed.GetFeedInteractor;
import com.onrushers.domain.business.interactor.report.CreateReportFeedInteractor;
import com.onrushers.domain.business.interactor.user.GetUsersInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IFeedReport;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by Ludovic on 01/09/16.
 */
public class FeedDetailPresenterImpl implements FeedDetailPresenter {

	private final GetAuthSessionInteractor   mGetAuthSessionInteractor;
	private final GetFeedInteractor          mGetFeedInteractor;
	private final CreateReportFeedInteractor mCreateReportFeedInteractor;
	private final DeleteFeedInteractor       mDeleteFeedInteractor;
	private final GetFeedCommentsInteractor  mGetFeedCommentsInteractor;
	private final GetUsersInteractor         mGetUsersInteractor;
	private final CreateCommentInteractor    mCreateCommentInteractor;
	private final DeleteCommentInteractor    mDeleteComemntInteractor;
	private final AddBoostInteractor         mAddBoostInteractor;
	private final DeleteBoostInteractor      mDeleteBoostInteractor;

	private FeedDetailView mView;
	private Integer        mUserId;

	private IFeed mFeed;
	private int mCommentsPage = 1;
	private boolean mLazyFeed = false;

	@Inject
	public FeedDetailPresenterImpl(GetAuthSessionInteractor getAuthSessionInteractor,
	                               GetFeedInteractor getFeedInteractor,
	                               CreateReportFeedInteractor createReportFeedInteractor,
	                               DeleteFeedInteractor deleteFeedInteractor,
	                               GetFeedCommentsInteractor getFeedCommentsInteractor,
	                               GetUsersInteractor getUsersInteractor,
	                               CreateCommentInteractor createCommentInteractor,
	                               DeleteCommentInteractor deleteCommentInteractor,
	                               AddBoostInteractor addBoostInteractor,
	                               DeleteBoostInteractor deleteBoostInteractor) {

		mGetAuthSessionInteractor = getAuthSessionInteractor;
		mGetFeedInteractor = getFeedInteractor;
		mCreateReportFeedInteractor = createReportFeedInteractor;
		mDeleteFeedInteractor = deleteFeedInteractor;
		mGetFeedCommentsInteractor = getFeedCommentsInteractor;
		mGetUsersInteractor = getUsersInteractor;
		mCreateCommentInteractor = createCommentInteractor;
		mDeleteComemntInteractor = deleteCommentInteractor;
		mAddBoostInteractor = addBoostInteractor;
		mDeleteBoostInteractor = deleteBoostInteractor;
	}

	//region FeedDetailPresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setView(FeedDetailView view) {
		mView = view;

		mGetAuthSessionInteractor.execute(new DefaultSubscriber<IAuthSession>() {
			@Override
			public void onNext(IAuthSession authSession) {
				mUserId = authSession.getUserId();
			}
		});
	}

	@Override
	public void setFeed(IFeed feed) {
		if (feed.isLazy()) {
			mLazyFeed = true;
			mGetFeedInteractor.setFeed(feed);
			mGetFeedInteractor.execute(new DefaultSubscriber<IFeed>() {
				@Override public void onNext(IFeed loadedFeed) {
					setFeed(loadedFeed);
				}

				@Override public void onError(Throwable e) {
					mView.showMessage(R.string.feed_messages_not_found);
					mView.dismissView();
				}
			});
		} else {
			mFeed = feed;

			if (mLazyFeed) {
				mView.updateFeed(feed);
				mLazyFeed = false;
			}

			mCreateReportFeedInteractor.setFeed(feed);
			mDeleteFeedInteractor.setFeed(feed);
			mGetFeedCommentsInteractor.setFeed(feed);
			mCreateCommentInteractor.setFeed(feed);

			getCommentsAtPage(1);
		}
	}

	@Override
	public void reportFeed() {
		mCreateReportFeedInteractor.execute(new ReportFeedSubscriber());
	}

	@Override
	public void deleteFeed() {
		mDeleteFeedInteractor.execute(new DeleteFeedSubscriber());
	}

	@Override
	public FeedDetailViewType getViewType() {
		if (mFeed == null) {
			return null;
		}
		switch (mFeed.getType()) {
			case Post:
				return new FeedDetailViewPost();
			case Register:
				return new FeedDetailViewRegister();
			case AvatarChange:
				return new FeedDetailViewAvatarChange();
			default:
				return null;
		}
	}

	@Override
	public void getCommentsAtPage(int page) {
		mCommentsPage = page;
		mGetFeedCommentsInteractor.setPage(mCommentsPage);
		mGetFeedCommentsInteractor.execute(new GetCommentsSubscriber());
	}

	@Override
	public void addComment(String text) {
		mCreateCommentInteractor.setText(text);
		mCreateCommentInteractor.execute(new CreateCommentSubscriber());
	}

	@Override
	public void deleteComment(IComment comment) {
		mDeleteComemntInteractor.setComment(comment);
		mDeleteComemntInteractor.execute(new DeleteCommentSubscriber());
	}

	@Override
	public void addRush(IFeed feed) {
		mAddBoostInteractor.setNumber(1);
		mAddBoostInteractor.setFeed(feed);
		mAddBoostInteractor.execute(new AddBoostSubscriber(feed));
	}

	@Override
	public void removeRush(IFeed feed, IBoost boost) {
		if (boost == null) {
			return;
		}
		mDeleteBoostInteractor.setBoost(boost);
		mDeleteBoostInteractor.execute(new DeleteBoostSubscriber(feed));
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	private final class ReportFeedSubscriber extends DefaultSubscriber<IFeedReport> {

		@Override
		public void onError(Throwable e) {
			mView.showReportFailed();
		}

		@Override
		public void onNext(IFeedReport feedReport) {
			mView.showReportSent();
		}
	}

	private final class DeleteFeedSubscriber extends DefaultSubscriber<IGenericResult> {

		@Override
		public void onNext(IGenericResult result) {
			BusProvider.getInstance().post(new DeleteFeedEvent(mFeed));

			if (result.isSuccess()) {
				mView.dismissView();
			}
		}
	}

	private final class GetCommentsSubscriber extends DefaultSubscriber<IPagination<IComment>> {

		@Override
		public void onNext(IPagination<IComment> pagination) {
			mView.showComments(pagination.getItems(), mCommentsPage);

			Set<Integer> userIdsSet = new HashSet<>();
			if (mUserId != null) {
				userIdsSet.add(mUserId);
			}

			Iterator<IComment> iterator = pagination.getItems().iterator();

			while (iterator.hasNext()) {
				userIdsSet.add(iterator.next().getUserId());
			}

			if (!userIdsSet.isEmpty()) {
				mGetUsersInteractor.setUserIds(new ArrayList<>(userIdsSet));
				mGetUsersInteractor.execute(new GetUsersSubscriber());
			}
		}
	}

	private final class GetUsersSubscriber extends DefaultSubscriber<List<IUser>> {

		@Override
		public void onNext(List<IUser> users) {
			mView.showCommentUsers(users);
		}
	}

	private final class CreateCommentSubscriber extends DefaultSubscriber<IComment> {

		@Override
		public void onNext(IComment comment) {
			comment.compareWithUserId(mUserId);

			List<IComment> commentList = new ArrayList<>();
			commentList.add(comment);

			mView.showComments(commentList, mCommentsPage);
			mView.showCommentSent();
		}
	}

	private final class DeleteCommentSubscriber extends DefaultSubscriber<IGenericResult> {

		@Override
		public void onError(Throwable e) {
			mView.showCommentDeleteFailed();
		}

		@Override
		public void onNext(IGenericResult result) {
			if (result.isSuccess()) {
				getCommentsAtPage(1);
				mView.showCommentDeleted();
				/** tell the adapter to remove specific comment and reload views */
			} else {
				mView.showCommentDeleteFailed();
			}
		}
	}

	protected final class AddBoostSubscriber extends DefaultSubscriber<IBoost> {

		private final IFeed mFeed;

		AddBoostSubscriber(IFeed feed) {
			mFeed = feed;
		}

		@Override
		public void onNext(IBoost boost) {
			if (boost.getId() != null) {
				mFeed.attachRush(boost);
				mView.reloadView();
			}
		}
	}

	protected final class DeleteBoostSubscriber extends DefaultSubscriber<IGenericResult> {

		private final IFeed mFeed;

		DeleteBoostSubscriber(IFeed feed) {
			mFeed = feed;
		}

		@Override
		public void onNext(IGenericResult result) {
			if (result.isSuccess()) {
				mFeed.attachRush(null);
				mView.reloadView();
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
