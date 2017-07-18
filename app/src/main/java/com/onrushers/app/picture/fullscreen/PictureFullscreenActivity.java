package com.onrushers.app.picture.fullscreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawableResource;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.activities.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PictureFullscreenActivity extends BaseActivity {

	@Bind(R.id.picture_fullscreen_toolbar)
	Toolbar mToolbar;

	@Bind(R.id.picture_fullscreen_imageview)
	ImageView mImageView;

	private PhotoViewAttacher mViewAttacher;

	private String mPictureUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_fullscreen);
		ButterKnife.bind(this);

		final Intent intent = getIntent();
		if (intent != null && intent.hasExtra(Extra.PICTURE_URL)) {
			mPictureUrl = intent.getStringExtra(Extra.PICTURE_URL);
		}
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});

		if (mPictureUrl == null) {
			return;
		}

		mImageView.setImageResource(R.drawable.ic_user_avatar_default);

		Glide.with(this)
			.load(mPictureUrl)
			.asBitmap()
			.into(new BitmapImageViewTarget(mImageView) {

				@Override
				protected void setResource(Bitmap resource) {
					super.setResource(resource);
					mImageView.setImageBitmap(resource);

					mViewAttacher = new PhotoViewAttacher(mImageView);
				}
			});
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
		outState.putString(Extra.PICTURE_URL, mPictureUrl);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onRestoreInstanceState(savedInstanceState, persistentState);
		mPictureUrl = savedInstanceState.getString(Extra.PICTURE_URL);
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		replaceFragment(R.id.picture_fullscreen_content, fragment, tag);
	}

	@Override
	public void onBackPressed() {
		if (Build.VERSION.SDK_INT >= 21) {
			supportFinishAfterTransition();
		} else {
			finish();
		}
	}
}
