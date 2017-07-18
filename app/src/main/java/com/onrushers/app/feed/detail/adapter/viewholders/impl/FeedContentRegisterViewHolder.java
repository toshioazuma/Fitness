package com.onrushers.app.feed.detail.adapter.viewholders.impl;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.onrushers.app.R;
import com.onrushers.app.feed.detail.adapter.viewholders.IFeedViewHolder;
import com.onrushers.app.file.Downloader;
import com.onrushers.app.file.FileClient;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ludovic on 04/09/16.
 */
public class FeedContentRegisterViewHolder extends RecyclerView.ViewHolder
	implements IFeedViewHolder {

	@Bind(R.id.card_feed_detail_avatar_imageview)
	CircularImageView mAvatarImageView;

	@Bind(R.id.card_feed_detail_user_name_textview)
	TextView mUsernameTextView;

	private FeedContentRegisterViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	public static final FeedContentRegisterViewHolder newInstance(ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.card_feed_detail_content_register, parent,
			false);

		return new FeedContentRegisterViewHolder(itemView);
	}


	@Override
	public void setFeed(IFeed feed) {
		if (feed.getOwner() != null) {
			IUser owner = feed.getOwner();
			mUsernameTextView.setText(owner.getUsername());

			if (owner.getProfilePicture() != null) {
				String fileUrl = Downloader.Companion.getInstance().resourceUrl(owner.getProfilePicture());

				Glide.with(itemView.getContext())
					.load(fileUrl)
					.asBitmap()
					.centerCrop()
					.placeholder(R.drawable.ic_user_avatar_default)
					.into(mAvatarImageView);
			}
		}
	}
}
