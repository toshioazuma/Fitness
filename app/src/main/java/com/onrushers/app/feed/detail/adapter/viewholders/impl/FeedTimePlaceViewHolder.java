package com.onrushers.app.feed.detail.adapter.viewholders.impl;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onrushers.app.R;
import com.onrushers.app.feed.detail.adapter.viewholders.IFeedViewHolder;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.type.FeedType;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ludovic on 04/09/16.
 */
public class FeedTimePlaceViewHolder extends RecyclerView.ViewHolder implements IFeedViewHolder {

	@Bind(R.id.card_feed_detail_time_place_layout)
	LinearLayout mContainerLayout;

	@Bind(R.id.card_feed_detail_time_textview)
	TextView mTimeTextView;

	@Bind(R.id.card_feed_detail_place_textview)
	TextView mPlaceTextView;


	private FeedTimePlaceViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	public static final FeedTimePlaceViewHolder newInstance(ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.card_feed_detail_time_place, parent, false);

		return new FeedTimePlaceViewHolder(itemView);
	}

	@Override
	public void setFeed(IFeed feed) {
		if (feed.getType() == FeedType.Register) {
			mContainerLayout.setGravity(Gravity.CENTER);
		} else {
			mContainerLayout.setGravity(Gravity.LEFT);
		}

		if (feed.getCreatedAt() != null) {
			CharSequence timeAgo = DateUtils.getRelativeDateTimeString(itemView.getContext(),
				feed.getCreatedAt().getTime(), 0, DateUtils.WEEK_IN_MILLIS, 0);

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
	}
}
