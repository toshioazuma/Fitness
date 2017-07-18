package com.onrushers.app.feed.detail.adapter.viewholders.impl;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.onrushers.app.R;
import com.onrushers.app.feed.detail.adapter.viewholders.IFeedViewHolder;
import com.onrushers.app.file.Downloader;
import com.onrushers.domain.business.model.IFeed;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ludovic on 04/09/16.
 */
public class FeedContentPictureViewHolder extends RecyclerView.ViewHolder implements IFeedViewHolder {

	@Bind(R.id.card_feed_detail_picture_imageview)
	ImageView mPictureImageView;

	private FeedContentPictureViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	public static final FeedContentPictureViewHolder newInstance(ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.card_feed_detail_content_picture, parent, false);

		return new FeedContentPictureViewHolder(itemView);
	}

	public ImageView getPictureImageView() {
		return mPictureImageView;
	}

	@Override
	public void setFeed(IFeed feed) {

		if (feed.getPhotos() != null && !feed.getPhotos().isEmpty()) {
			Integer pictureId = feed.getPhotos().get(0);
			String fileUrl = Downloader.Companion.getInstance().resourceUrl(pictureId);

			Glide.with(itemView.getContext())
				.load(fileUrl)
				.centerCrop()
				.crossFade()
				.placeholder(R.drawable.ic_default_placeholder)
				.into(mPictureImageView);
		}
	}
}
