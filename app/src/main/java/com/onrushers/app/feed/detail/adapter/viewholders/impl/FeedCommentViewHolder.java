package com.onrushers.app.feed.detail.adapter.viewholders.impl;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.onrushers.app.R;
import com.onrushers.app.feed.detail.OnCommentDeleteListener;
import com.onrushers.app.feed.detail.adapter.viewholders.ICommentViewHolder;
import com.onrushers.app.feed.detail.adapter.viewholders.IUserViewHolder;
import com.onrushers.app.file.Downloader;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedCommentViewHolder extends RecyclerView.ViewHolder
	implements ICommentViewHolder, IUserViewHolder {

	@Bind(R.id.card_feed_detail_comment_avatar_imageview)
	CircularImageView mAvatarImageView;

	@Bind(R.id.card_feed_detail_comment_option_button)
	ImageButton mOptionButton;

	@Bind(R.id.card_feed_detail_comment_time_textview)
	TextView mTimeTextView;

	@Bind(R.id.card_feed_detail_comment_user_textview)
	TextView mUsernameTextView;

	@Bind(R.id.card_feed_detail_comment_text_textview)
	TextView mCommentTextView;

	private IComment mComment;

	private final OnCommentDeleteListener mCommentDeleteListener;


	private FeedCommentViewHolder(View itemView, OnCommentDeleteListener commentDeleteListener) {
		super(itemView);
		ButterKnife.bind(this, itemView);
		mCommentDeleteListener = commentDeleteListener;

		mTimeTextView.setText("");
		mUsernameTextView.setText("");
		mCommentTextView.setText("");
	}

	public static final FeedCommentViewHolder newInstance(ViewGroup parent, OnCommentDeleteListener commentDeleteListener) {

		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.card_feed_detail_comment, parent, false);

		return new FeedCommentViewHolder(itemView, commentDeleteListener);
	}

	@Override
	public void setComment(IComment comment) {
		mComment = comment;

		CharSequence timeAgo = DateUtils.getRelativeDateTimeString(
			itemView.getContext(), comment.getDate().getTime(), 0, DateUtils.WEEK_IN_MILLIS, 0);

		mCommentTextView.setText(comment.getText());
		mTimeTextView.setText(timeAgo);

		if (comment.isMine()) {
			mOptionButton.setVisibility(View.VISIBLE);
		} else {
			mOptionButton.setVisibility(View.GONE);
		}
	}

	@Override
	public void setUser(IUser user) {
		mUsernameTextView.setText(user.getUsername());

		if (!TextUtils.isEmpty(user.getProfilePicture())) {
			String fileUrl = Downloader.Companion.getInstance().resourceUrl(user.getProfilePicture());

			Glide.with(itemView.getContext())
				.load(fileUrl)
				.centerCrop()
				.crossFade()
				.placeholder(R.drawable.ic_user_avatar_default)
				.into(mAvatarImageView);

		} else {
			mAvatarImageView.setImageResource(R.drawable.ic_user_avatar_default);
		}

	}

	@OnClick(R.id.card_feed_detail_comment_option_button)
	public void onDeleteOptionClick() {

		PopupMenu popupMenu = new PopupMenu(itemView.getContext(), mOptionButton);
		popupMenu.getMenuInflater().inflate(R.menu.card_comment_options, popupMenu.getMenu());
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.menu_comment_row_delete:
						if (mCommentDeleteListener != null) {
							mCommentDeleteListener.onDeleteComment(mComment);
						}
						return true;
				}
				return false;
			}
		});
		popupMenu.show();
	}
}
