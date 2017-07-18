package com.onrushers.app.feed.adapters.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.onrushers.app.R;
import com.onrushers.app.feed.adapters.OnFeedDeleteListener;
import com.onrushers.app.feed.adapters.OnFeedHeaderListener;
import com.onrushers.app.feed.adapters.OnFeedReportListener;
import com.onrushers.app.file.Downloader;
import com.onrushers.app.file.FileClient;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.FeedType;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TODO: document your custom view class.
 */
public class FeedHeaderView extends RelativeLayout {

	@Bind(R.id.feed_header_avatar_imageview)
	ImageView mAvatarImageView;

	@Bind(R.id.feed_header_options_button)
	ImageButton mOptionsButton;

	@Bind(R.id.feed_header_username_textview)
	TextView mUsernameTextView;

	@Bind(R.id.feed_header_grade_textview)
	TextView mGradeTextView;

	private IFeed mFeed;

	private OnFeedHeaderListener mListener;
	private OnFeedReportListener mReportListener;
	private OnFeedDeleteListener mDeleteListener;


	public FeedHeaderView(Context context) {
		super(context);
		init(null, 0);
	}

	public FeedHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public FeedHeaderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		super.inflate(getContext(), R.layout.view_feed_header, this);
		ButterKnife.bind(this);
	}

	public void setFeed(IFeed feed) {
		mFeed = feed;

		if (feed.getOwner() != null) {
			IUser owner = feed.getOwner();
			mUsernameTextView.setText(owner.getUsername());
			mGradeTextView.setText(owner.getGradeString());
			mAvatarImageView.setImageResource(R.drawable.ic_user_avatar_default);

			if (owner.getProfilePicture() != null) {
				String fileUrl = Downloader.Companion.getInstance().resourceUrl(owner.getProfilePicture());

				Glide.with(mAvatarImageView.getContext())
					.load(fileUrl)
					.asBitmap()
					.centerCrop()
					.into(mAvatarImageView);
			}
		} else {
			mAvatarImageView.setImageResource(R.drawable.ic_user_avatar_default);
		}

		if (feed.getType() == FeedType.Post) {
			mOptionsButton.setVisibility(VISIBLE);
		} else {
			mOptionsButton.setVisibility(GONE);
		}
	}

	public void hideOptions() {
		mOptionsButton.setVisibility(GONE);
	}

	public void setFeedHeaderListener(OnFeedHeaderListener listener) {
		mListener = listener;
	}

	public void setOnFeedReportListener(OnFeedReportListener reportListener) {
		mReportListener = reportListener;
	}

	public void setOnFeedDeleteListener(OnFeedDeleteListener deleteListener) {
		mDeleteListener = deleteListener;
	}

	public ImageView getAvatarImageView() {
		return mAvatarImageView;
	}

	//region Buttons click
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.feed_header_options_button)
	public void onOptionsClick() {

		final Context context = getContext();

		int menuRes;
		if (mFeed.isMine()) {
			menuRes = R.menu.card_feed_detail_my_options;
		} else {
			menuRes = R.menu.card_feed_detail_other_options;
		}

		PopupMenu popup = new PopupMenu(context, mOptionsButton);
		popup.getMenuInflater().inflate(menuRes, popup.getMenu());

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {

				switch (item.getItemId()) {
					case R.id.menu_feed_detail_report:
						if (mReportListener != null) {
							mReportListener.onReportFeed(mFeed);
						}
						return true;

					case R.id.menu_feed_detail_delete:
						if (mDeleteListener != null) {
							mDeleteListener.onDeleteFeed(mFeed);
						}
						return true;
				}
				return false;
			}
		});
		popup.show();
	}

	@OnClick({R.id.feed_header_avatar_imageview, R.id.feed_header_username_textview})
	public void onUsernameClick() {
		if (mListener != null) {
			mListener.onShowUserProfilePage(mFeed.getOwner());
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
