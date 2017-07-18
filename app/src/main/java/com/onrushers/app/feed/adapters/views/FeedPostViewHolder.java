package com.onrushers.app.feed.adapters.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.onrushers.app.R;
import com.onrushers.app.common.Constant;
import com.onrushers.app.feed.adapters.OnFeedDeleteListener;
import com.onrushers.app.feed.adapters.OnFeedHeaderListener;
import com.onrushers.app.feed.adapters.OnFeedReportListener;
import com.onrushers.app.feed.adapters.OnFeedRushListener;
import com.onrushers.app.feed.adapters.OnFeedRushesViewListener;
import com.onrushers.app.feed.detail.OnFeedDetailListener;
import com.onrushers.app.file.Downloader;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.model.IUserTag;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class FeedPostViewHolder extends FeedViewHolder {
	
	@Bind(R.id.card_feed_photos_container)
	RelativeLayout mPhotosContainer;
	
	@Bind(R.id.card_feed_photo_imageview)
	ImageView mPhotoImageView;
	
	@Bind(R.id.card_feed_tag_users_view)
	FeedTagUsersView mTagUsersView;
	
	@Bind(R.id.card_feed_delete_button)
	Button mDeleteButton;
	
	public FeedPostViewHolder(View itemView, OnFeedHeaderListener headerListener,
	                          OnFeedRushListener rushListener, OnFeedRushesViewListener rushesViewListener,
	                          OnFeedDetailListener detailListener, OnFeedReportListener reportListener,
	                          OnFeedDeleteListener deleteListener) {
		
		super(itemView, headerListener, rushListener,
			detailListener, reportListener, deleteListener, rushesViewListener);
	}
	
	@Override
	public void setFeed(IFeed feed) {
		super.setFeed(feed);
		
		if (feed.getPhotos() != null && feed.getPhotos().size() > 0) {
			Integer photoId = feed.getPhotos().get(0);

			String fileUrl = Downloader.Companion.getInstance().resourceUrl(photoId);

			Glide.with(mPhotoImageView.getContext())
				.load(fileUrl)
				.asBitmap()
				.centerCrop()
				.into(mPhotoImageView);

			mPhotosContainer.setVisibility(View.VISIBLE);
		} else {
			mPhotosContainer.setVisibility(View.GONE);
		}
		
		if (!TextUtils.isEmpty(feed.getContent())) {
			String content = feed.getContent();
			
			final Context ctx = itemView.getContext();
			
			/** handle case of change profile picture */
			if (content.contains(Constant.PATTERN_CHANGE_PROFILE_PICTURE)) {
				mContentTextView.setText(ctx.getString(
					R.string.feed_post_change_profile_picture_text, feed.getOwner().getUsername()));
			}
			/** handle case of change cover picture */
			else if (content.contains(Constant.PATTERN_CHANGE_COVER_PICTURE)) {
				mContentTextView.setText(ctx.getString(
					R.string.feed_post_change_cover_picture_text, feed.getOwner().getUsername()));
			}
		}
		
		List tagsUsers = feed.getTagsUsers();
		if (tagsUsers != null && !tagsUsers.isEmpty()) {

			Observable.from((List<IUserTag>) tagsUsers)
				.map(new Func1<IUserTag, IUser>() {
					@Override
					public IUser call(IUserTag userTag) {
						return userTag.getUser();
					}
				})
				.toList()
				.subscribe(new Action1<List<IUser>>() {
					@Override
					public void call(List<IUser> users) {
						mTagUsersView.setUsers(users);
						mTagUsersView.setVisibility(View.VISIBLE);
					}
				});
		} else {
			mTagUsersView.setVisibility(View.GONE);
		}
	}
	
	//region OnClick
	//----------------------------------------------------------------------------------------------
	
	@OnClick(R.id.card_feed_photo_imageview)
	public void onImageClick() {
		if (mDetailListener != null) {
			mDetailListener.onFeedDetail(mFeed, mPhotoImageView);
		}
	}
	
	@OnClick(R.id.card_feed_delete_button)
	public void onDeleteClick() {
		
		if (mDeleteListener != null) {
			mDeleteListener.onDeleteFeed(mFeed);
		}
	}
	
	//----------------------------------------------------------------------------------------------
	//endregion
}
