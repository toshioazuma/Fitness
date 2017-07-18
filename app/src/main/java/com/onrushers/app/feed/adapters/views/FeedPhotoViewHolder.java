package com.onrushers.app.feed.adapters.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.onrushers.app.R;
import com.onrushers.app.feed.detail.OnFeedDetailListener;
import com.onrushers.app.file.Downloader;
import com.onrushers.domain.business.model.IFeed;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

	private final OnFeedDetailListener mFeedDetailListener;

	@Bind(R.id.card_feed_post_photo_imageview)
	ImageView mImageView;

	private IFeed mFeed;


	public FeedPhotoViewHolder(View itemView, OnFeedDetailListener listener) {
		super(itemView);
		ButterKnife.bind(this, itemView);
		itemView.setOnClickListener(this);

		mFeedDetailListener = listener;
	}

	public void setFeed(IFeed feed) {
		mFeed = feed;

		if (feed.getPhotos() != null && !feed.getPhotos().isEmpty()) {
			final Integer photoId = feed.getPhotos().get(0);

			String fileUrl = Downloader.Companion.getInstance().resourceUrl(photoId);

			Glide.with(mImageView.getContext())
				.load(fileUrl)
				.asBitmap()
				.centerCrop()
				.placeholder(R.drawable.ic_default_placeholder)
				.into(mImageView);

		} else {
			mImageView.setImageResource(R.drawable.ic_default_placeholder);
		}
	}

	//region View.OnClickListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onClick(View view) {
		if (mFeedDetailListener != null) {
			mFeedDetailListener.onFeedDetail(mFeed, mImageView);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
