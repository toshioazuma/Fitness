package com.onrushers.app.feed.adapters.views;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.onrushers.app.R;
import com.onrushers.app.feed.adapters.OnFeedDeleteListener;
import com.onrushers.app.feed.adapters.OnFeedHeaderListener;
import com.onrushers.app.feed.adapters.OnFeedReportListener;
import com.onrushers.app.feed.adapters.OnFeedRushListener;
import com.onrushers.app.feed.adapters.OnFeedRushesViewListener;
import com.onrushers.app.feed.detail.OnFeedDetailListener;
import com.onrushers.app.file.Downloader;
import com.onrushers.app.user.OnUserDetailListener;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;

import butterknife.Bind;
import butterknife.OnClick;

public class FeedRegisterViewHolder extends FeedViewHolder {

	@Bind(R.id.card_feed_avatar_imageview)
	CircularImageView mAvatarImageView;

	@Bind(R.id.card_feed_user_name_textview)
	TextView mUserNameTextView;

	OnUserDetailListener mUserDetailListener;

	public FeedRegisterViewHolder(View itemView, OnFeedHeaderListener headerListener,
		OnFeedRushListener rushListener, OnFeedRushesViewListener rushesViewListener,
		OnFeedDetailListener detailListener, OnFeedReportListener reportListener,
		OnFeedDeleteListener deleteListener, OnUserDetailListener userDetailListener) {

		super(itemView, headerListener, rushListener, detailListener, reportListener,
			deleteListener, rushesViewListener);

		mUserDetailListener = userDetailListener;
	}

	@Override
	public void setFeed(IFeed feed) {
		super.setFeed(feed);

		if (feed.getOwner() != null) {
			IUser owner = feed.getOwner();
			mUserNameTextView.setText(owner.getUsername());
			mContentTextView.setText(R.string.feed_register_welcome_text);

			if (owner.getProfilePicture() != null) {
				String fileUrl = Downloader.Companion.getInstance().resourceUrl(owner.getProfilePicture());

				Glide.with(mAvatarImageView.getContext())
					.load(fileUrl)
					.centerCrop()
					.crossFade()
					.placeholder(R.drawable.ic_user_avatar_default)
					.into(mAvatarImageView);
			}
		} else {
			mAvatarImageView.setImageResource(R.drawable.ic_user_avatar_default);
		}
	}

	@OnClick(R.id.card_feed_content_container_layout)
	public void onContentContainerClick() {

		if (mUserDetailListener != null && mFeed.getOwner() != null) {
			mUserDetailListener.onUserDetail(mFeed.getOwner());
		}
	}
}
