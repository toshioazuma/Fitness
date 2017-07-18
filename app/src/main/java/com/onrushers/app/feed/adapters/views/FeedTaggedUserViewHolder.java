package com.onrushers.app.feed.adapters.views;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.onrushers.app.R;
import com.onrushers.app.file.Downloader;
import com.onrushers.app.file.FileClient;
import com.onrushers.domain.business.model.IUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedTaggedUserViewHolder extends RecyclerView.ViewHolder {

	@Bind(R.id.card_feed_tag_user_imageview)
	CircularImageView mAvatarImageView;

	public static final FeedTaggedUserViewHolder newInstance(ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.card_feed_tag_user, parent, false);

		return new FeedTaggedUserViewHolder(itemView);
	}

	private FeedTaggedUserViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	public void setUser(IUser user) {

		if (!TextUtils.isEmpty(user.getProfilePicture())) {
			String fileUrl = Downloader.Companion.getInstance().resourceUrl(user.getProfilePicture());

			Glide.with(mAvatarImageView.getContext())
				.load(fileUrl)
				.asBitmap()
				.centerCrop()
				.into(mAvatarImageView);

		} else {
			mAvatarImageView.setImageResource(R.drawable.ic_user_avatar_default);
		}
	}
}
