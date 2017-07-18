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
import com.onrushers.app.feed.adapters.OnUserSuggestionListener;
import com.onrushers.app.feed.detail.OnFeedDetailListener;
import com.onrushers.app.file.Downloader;
import com.onrushers.app.user.OnUserDetailListener;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;

import butterknife.Bind;
import butterknife.OnClick;

public class FeedHeroSuggestionViewHolder extends FeedViewHolder {

	@Bind(R.id.card_feed_avatar_imageview)
	CircularImageView mAvatarImageView;

	@Bind(R.id.card_feed_user_name_textview)
	TextView mUserNameTextView;

	OnUserDetailListener     mUserDetailListener;
	OnUserSuggestionListener mUserSuggestionListener;

	public FeedHeroSuggestionViewHolder(View itemView, OnUserDetailListener userDetailListener, OnUserSuggestionListener userSuggestionListener) {
		super(itemView, null, null, null, null, null, null);

		mUserDetailListener = userDetailListener;
		mUserSuggestionListener = userSuggestionListener;
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
		if (mUserDetailListener == null || mFeed == null || mFeed.getOwner() == null) {
			return;
		}
		mUserDetailListener.onUserDetail(mFeed.getOwner());
	}

	@OnClick(R.id.card_feed_skip_button)
	public void onSkipFeed() {
		if (mUserSuggestionListener == null || mFeed == null) {
			return;
		}
		mUserSuggestionListener.onSkipHeroSuggestion(mFeed);
	}

	@OnClick(R.id.card_feed_hero_follow_button)
	public void onFollowUser() {
		if (mUserSuggestionListener == null || mFeed == null) {
			return;
		}
		mUserSuggestionListener.onFollowHeroSuggestion(mFeed);
	}
}
