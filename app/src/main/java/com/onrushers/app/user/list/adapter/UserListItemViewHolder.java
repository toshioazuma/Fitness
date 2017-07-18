package com.onrushers.app.user.list.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.onrushers.app.R;
import com.onrushers.app.file.Downloader;
import com.onrushers.domain.business.model.IUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserListItemViewHolder {

	@Bind(R.id.card_user_list_item_picture_imageview)
	protected CircularImageView mUserImageView;

	@Bind(R.id.card_user_list_item_follow_button)
	protected Button mFollowButton;

	@Bind(R.id.card_user_list_item_username_textview)
	protected TextView mUsernameTextView;

	@Bind(R.id.card_user_list_item_grade_textview)
	protected TextView mGradeTextView;


	public UserListItemViewHolder(View itemView) {
		ButterKnife.bind(this, itemView);
	}

	public void setUser(IUser user) {

		mUsernameTextView.setText(user.getUsername());
		mGradeTextView.setText(user.getGradeString());

		if (!TextUtils.isEmpty(user.getProfilePicture())) {
			String fileUrl = Downloader.Companion.getInstance().resourceUrl(user.getProfilePicture());

			Glide.with(mUserImageView.getContext())
				.load(fileUrl)
				.fitCenter()
				.crossFade()
				.into(mUserImageView);
		} else {
			mUserImageView.setImageResource(R.drawable.ic_user_avatar_default);
		}
	}
}
