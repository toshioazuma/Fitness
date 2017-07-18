package com.onrushers.app.feed.detail.adapter.viewholders.impl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onrushers.app.R;
import com.onrushers.app.common.Constant;
import com.onrushers.app.feed.detail.adapter.viewholders.IFeedViewHolder;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.type.FeedType;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ludovic on 04/09/16.
 */
public class FeedContentTextViewHolder extends RecyclerView.ViewHolder implements IFeedViewHolder {

	@Bind(R.id.card_feed_detail_content_textview)
	TextView mTextView;

	private FeedContentTextViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	public static final FeedContentTextViewHolder newInstance(ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.card_feed_detail_content_text, parent, false);

		return new FeedContentTextViewHolder(itemView);
	}

	@Override
	public void setFeed(IFeed feed) {
		if (feed.getType() == FeedType.Register) {
			mTextView.setGravity(Gravity.CENTER);
			mTextView.setText(R.string.feed_register_welcome_text);
		} else if (feed.getType() == FeedType.AvatarChange) {
			mTextView.setGravity(Gravity.CENTER);
			mTextView.setText(R.string.feed_avatar_change_text);
		} else {
			mTextView.setGravity(Gravity.LEFT);

			String content = feed.getContent();

			if (feed.getType() == FeedType.Post) {

				final Context ctx = itemView.getContext();

				/** handle case of change profile picture */
				if (content.contains(Constant.PATTERN_CHANGE_PROFILE_PICTURE)) {
					mTextView.setText(ctx.getString(
						R.string.feed_post_change_profile_picture_text, feed.getOwner().getFirstName()));
				}
				/** handle case of change cover picture */
				else if (content.contains(Constant.PATTERN_CHANGE_COVER_PICTURE)) {
					mTextView.setText(ctx.getString(
						R.string.feed_post_change_cover_picture_text, feed.getOwner().getFirstName()));
				} else {
					mTextView.setText(content);
				}
			} else {
				mTextView.setText(content);
			}
		}
	}
}
