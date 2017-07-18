package com.onrushers.app.feed.detail.adapter.viewholders.impl;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.onrushers.app.R;
import com.onrushers.app.feed.adapters.OnFeedRushListener;
import com.onrushers.app.feed.adapters.OnFeedRushesViewListener;
import com.onrushers.app.feed.detail.adapter.viewholders.IFeedViewHolder;
import com.onrushers.domain.business.model.IFeed;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ludovic on 03/09/16.
 */
public class FeedCountRushCommentViewHolder extends RecyclerView.ViewHolder implements IFeedViewHolder {

	@Bind(R.id.card_feed_detail_rush_count_button)
	Button mRushCountButton;

	@Bind(R.id.card_feed_detail_comment_count_button)
	Button mCommentCountButton;

	@Bind(R.id.card_feed_detail_rush_button)
	Button mRushButton;

	private final OnFeedRushListener       mRushListener;
	private final OnFeedRushesViewListener mRushesViewListener;

	private IFeed mFeed;

	private FeedCountRushCommentViewHolder(View itemView, OnFeedRushListener rushListener,
	                                       OnFeedRushesViewListener rushesViewListener) {
		super(itemView);
		ButterKnife.bind(this, itemView);
		mRushListener = rushListener;
		mRushesViewListener = rushesViewListener;
	}

	public static final FeedCountRushCommentViewHolder newInstance(
		ViewGroup parent, OnFeedRushListener rushListener, OnFeedRushesViewListener rushesViewListener) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.card_feed_detail_count_rush_comment, parent, false);

		return new FeedCountRushCommentViewHolder(itemView, rushListener, rushesViewListener);
	}

	@Override
	public void setFeed(IFeed feed) {
		mFeed = feed;
		mRushCountButton.setText(String.valueOf(feed.getBoostsCount()));
		mCommentCountButton.setText(String.valueOf(feed.getCommentsCount()));

		if (feed.isRushed()) {
			mRushButton.setSelected(true);
			mRushButton.setText(R.string.footer_feed_actions_undo_rush);
		} else {
			mRushButton.setSelected(false);
			mRushButton.setText(R.string.footer_feed_actions_rush);
		}
		mRushButton.setEnabled(true);
	}

	//region OnClick
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.card_feed_detail_rush_count_button)
	public void onRushCountClick() {
		if (mRushesViewListener != null) {
			mRushesViewListener.onViewRushesFeed(mFeed);
		}
	}

	@OnClick(R.id.card_feed_detail_rush_button)
	public void onRushClick() {

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
