package com.onrushers.app.feed.detail.adapter.viewholders.impl;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onrushers.app.R;
import com.onrushers.app.feed.adapters.OnFeedHeaderListener;
import com.onrushers.app.feed.adapters.views.FeedHeaderView;
import com.onrushers.app.feed.detail.adapter.viewholders.IFeedViewHolder;
import com.onrushers.app.file.FileClient;
import com.onrushers.domain.business.model.IFeed;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedHeaderViewHolder extends RecyclerView.ViewHolder implements IFeedViewHolder {

	@Bind(R.id.card_feed_detail_header_view)
	FeedHeaderView mHeaderView;

	private FeedHeaderViewHolder(
		View itemView, OnFeedHeaderListener feedHeaderListener) {

		super(itemView);
		ButterKnife.bind(this, itemView);
		mHeaderView.setFeedHeaderListener(feedHeaderListener);
	}

	public static final FeedHeaderViewHolder newInstance(ViewGroup parent, OnFeedHeaderListener feedHeaderListener) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.card_feed_detail_header, parent, false);

		return new FeedHeaderViewHolder(itemView, feedHeaderListener);
	}

	@Override
	public void setFeed(IFeed feed) {
		mHeaderView.setFeed(feed);
		mHeaderView.hideOptions();
	}
}
