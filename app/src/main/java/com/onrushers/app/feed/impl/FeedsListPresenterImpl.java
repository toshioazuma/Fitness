package com.onrushers.app.feed.impl;

import com.onrushers.app.feed.FeedsListPresenter;
import com.onrushers.app.feed.FeedsListView;
import com.onrushers.domain.business.interactor.boost.AddBoostInteractor;
import com.onrushers.domain.business.interactor.boost.DeleteBoostInteractor;
import com.onrushers.domain.business.interactor.feed.DeleteFeedInteractor;
import com.onrushers.domain.business.interactor.report.CreateReportFeedInteractor;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IFeedPagination;
import com.onrushers.domain.business.model.IFeedReport;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.List;

public abstract class FeedsListPresenterImpl implements FeedsListPresenter {

	private FeedsListView mFeedsListView;

	private int mPage    = 1;
	private int mMaxPage = 0;

	protected final DeleteFeedInteractor       mDeleteFeedInteractor;
	protected final CreateReportFeedInteractor mCreateReportFeedInteractor;
	protected final AddBoostInteractor         mAddBoostInteractor;
	protected final DeleteBoostInteractor      mDeleteBoostInteractor;

	public FeedsListPresenterImpl(DeleteFeedInteractor deleteFeedInteractor,
	                              CreateReportFeedInteractor createReportFeedInteractor,
	                              AddBoostInteractor addBoostInteractor,
	                              DeleteBoostInteractor deleteBoostInteractor) {

		mDeleteFeedInteractor = deleteFeedInteractor;
		mCreateReportFeedInteractor = createReportFeedInteractor;
		mAddBoostInteractor = addBoostInteractor;
		mDeleteBoostInteractor = deleteBoostInteractor;
	}

	protected void setView(FeedsListView feedsListView) {
		mFeedsListView = feedsListView;
	}

	protected abstract void onGetFeedsPage(int page);

	//region HomePresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void getFeedsAtPage(int page) {
		if (mMaxPage != 0 && page > mMaxPage) {
			return;
		}
		mPage = page;
		onGetFeedsPage(page);
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

	@Override
	public void deleteFeed(IFeed feed) {
		mDeleteFeedInteractor.setFeed(feed);
		mDeleteFeedInteractor.execute(new DeleteFeedSubscriber());
	}

	@Override
	public void reportFeed(IFeed feed) {
		mCreateReportFeedInteractor.setFeed(feed);
		mCreateReportFeedInteractor.execute(new ReportFeedSubscriber());
	}

	@Override
	public void onDestroy() {
		mDeleteFeedInteractor.unsubscribe();
		mCreateReportFeedInteractor.unsubscribe();
		mAddBoostInteractor.unsubscribe();
		mDeleteBoostInteractor.unsubscribe();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	public final class GetFeedsSubscriber extends DefaultSubscriber<IFeedPagination> {

		@Override
		public void onError(Throwable e) {
			mFeedsListView.showError(e.getLocalizedMessage());
		}

		@Override
		public void onNext(IFeedPagination feedPagination) {
			mMaxPage = feedPagination.getPages();
			mFeedsListView.showFeeds((List<IFeed>) feedPagination.getItems(), mPage);
		}
	}

	protected final class DeleteFeedSubscriber extends DefaultSubscriber<IGenericResult> {

		@Override
		public void onNext(IGenericResult result) {
			getFeedsAtPage(1);

			/**
			 * @todo: better solution
			 * tell the adapter to remove the specific feed from its source and reload views
			 */
		}
	}

	protected final class ReportFeedSubscriber extends DefaultSubscriber<IFeedReport> {

		@Override
		public void onError(Throwable e) {
			mFeedsListView.showReportSent(false);
		}

		@Override
		public void onNext(IFeedReport feedReport) {
			mFeedsListView.showReportSent(true);
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
				mFeedsListView.reloadView();
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
				mFeedsListView.reloadView();
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
