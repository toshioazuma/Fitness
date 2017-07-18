package com.onrushers.app.explore.tabs.ranking.views;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.onrushers.app.R;
import com.onrushers.app.file.Downloader;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.Gender;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExploreRankViewHolder {

	@Bind(R.id.explore_rank_position_textview)
	RankPositionTextView mPositionTextView;

	@Bind(R.id.explore_rank_gender_view)
	View mGenderView;

	@Bind(R.id.explore_rank_avatar_imageview)
	CircularImageView mAvatarImageView;

	@Bind(R.id.explore_rank_rush_count_textview)
	TextView mRushCountTextView;

	@Bind(R.id.explore_rank_username_textview)
	TextView mUsernameTextView;

	@Bind(R.id.explore_rank_grade_textview)
	TextView mGradeTextView;

	public ExploreRankViewHolder(View view) {
		ButterKnife.bind(this, view);
	}

	public void setUser(IUser user) {
		mPositionTextView.setRankPosition(user.getRank());
		mUsernameTextView.setText(user.getUsername());
		mGradeTextView.setText(user.getGradeString());
		mRushCountTextView.setText(String.valueOf(user.getBoostCount()));

		if (user.getGender() == Gender.Female) {
			mGenderView.setBackgroundResource(R.drawable.sh_round_pink);
		} else {
			mGenderView.setBackgroundResource(R.drawable.sh_round_blue);
		}

		if (!TextUtils.isEmpty(user.getProfilePicture())) {
			String fileUrl = Downloader.Companion.getInstance().resourceUrl(user.getProfilePicture());

			Glide.with(mAvatarImageView.getContext())
				.load(fileUrl)
				.asBitmap()
				.centerCrop()
				.placeholder(R.drawable.ic_user_avatar_default)
				.into(mAvatarImageView);
		} else {
			mAvatarImageView.setImageResource(R.drawable.ic_user_avatar_default);
		}
	}

}
