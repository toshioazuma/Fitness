package com.onrushers.app.feed.adapters.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onrushers.app.R;
import com.onrushers.app.feed.adapters.OnFeedDeleteListener;
import com.onrushers.app.feed.adapters.OnFeedHeaderListener;
import com.onrushers.app.feed.adapters.OnFeedReportListener;
import com.onrushers.app.feed.adapters.OnFeedRushListener;
import com.onrushers.app.feed.adapters.OnFeedRushesViewListener;
import com.onrushers.app.feed.detail.OnFeedDetailListener;
import com.onrushers.app.user.OnUserDetailListener;
import com.onrushers.domain.business.model.IFeed;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedViewHolder extends RecyclerView.ViewHolder {

	/**
	 * R.id.card_feed_header_view
	 */
	FeedHeaderView mFeedHeaderView;

	@Bind(R.id.card_feed_content_textview)
	TextView mContentTextView;

	@Bind(R.id.card_feed_time_textview)
	TextView mTimeTextView;

	@Bind(R.id.card_feed_place_textview)
	TextView mPlaceTextView;

	@Bind(R.id.card_feed_boost_button)
	Button mBoostButton;

	@Bind(R.id.card_feed_comment_button)
	Button mCommentButton;

	@Bind(R.id.card_feed_rush_button)
	Button mRushButton;

	protected Context mContext;

	protected OnFeedReportListener     mReportListener;
	protected OnFeedDeleteListener     mDeleteListener;
	protected OnFeedRushesViewListener mRushesViewListener;
	protected OnFeedDetailListener     mDetailListener;
	protected OnFeedRushListener       mRushListener;

	protected IFeed mFeed;

	public FeedViewHolder(View itemView, OnFeedHeaderListener headerListener, OnFeedRushListener rushListener,
	                      OnFeedDetailListener detailListener, OnFeedReportListener reportListener,
	                      OnFeedDeleteListener deleteListener, OnFeedRushesViewListener rushesViewListener) {

		super(itemView);
		mContext = itemView.getContext();
		mRushListener = rushListener;
		mDetailListener = detailListener;
		mReportListener = reportListener;
		mDeleteListener = deleteListener;
		mRushesViewListener = rushesViewListener;

		ButterKnife.bind(this, itemView);

		View headerView = itemView.findViewById(R.id.card_feed_header_view);
		if (headerView != null) {
			mFeedHeaderView = (FeedHeaderView) headerView;
			mFeedHeaderView.setFeedHeaderListener(headerListener);
			mFeedHeaderView.setOnFeedReportListener(reportListener);
			mFeedHeaderView.setOnFeedDeleteListener(deleteListener);
		}
	}

	public void onViewRecycled() {

	}

	public void setFeed(IFeed feed) {
		mFeed = feed;

		if (mFeedHeaderView != null) {
			mFeedHeaderView.setFeed(feed);
		}

		if (!TextUtils.isEmpty(feed.getContent())) {
			mContentTextView.setText(feed.getContent());
		}

		if (feed.getCreatedAt() != null) {
			CharSequence timeAgo = DateUtils.getRelativeDateTimeString(
				mContext, feed.getCreatedAt().getTime(), 0, DateUtils.WEEK_IN_MILLIS, 0);

			mTimeTextView.setText(timeAgo);
			mTimeTextView.setVisibility(View.VISIBLE);
		} else {
			mTimeTextView.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(feed.getPlace())) {
			mPlaceTextView.setText(feed.getPlace());
			mPlaceTextView.setVisibility(View.VISIBLE);
		} else {
			mPlaceTextView.setVisibility(View.GONE);
		}

		mBoostButton.setText(String.valueOf(feed.getBoostsCount()));
		mCommentButton.setText(String.valueOf(feed.getCommentsCount()));
		mRushButton.setEnabled(true);

		if (feed.isRushed()) {
			mRushButton.setSelected(true);
			mRushButton.setText(R.string.footer_feed_actions_undo_rush);
		} else {
			mRushButton.setSelected(false);
			mRushButton.setText(R.string.footer_feed_actions_rush);
		}
	}

	//region OnClick
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.card_feed_boost_button)
	public void onRushCountClick() {
		if (mRushesViewListener != null && mFeed.getBoostsCount() > 0) {
			mRushesViewListener.onViewRushesFeed(mFeed);
		}
	}

	@OnClick(R.id.card_feed_comment_button)
	public void onCommentCountClick() {
		if (mDetailListener != null) {
			mDetailListener.onFeedDetail(mFeed, null);
		}
	}

	@OnClick(R.id.card_feed_rush_button)
	public void onRushClick() {
		/** toggle state of rush button */

		mRushButton.setSelected(!mRushButton.isSelected());
		mRushButton.setEnabled(false);

		if (mRushButton.isSelected()) {
			mRushButton.setText(R.string.footer_feed_actions_undo_rush);
			if (mRushListener != null) {
				mRushListener.onRushFeed(mFeed);
			}
		} else {
			mRushButton.setText(R.string.footer_feed_actions_rush);
			if (mRushListener != null) {
				mRushListener.onUnrushFeed(mFeed, mFeed.getRush());
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
