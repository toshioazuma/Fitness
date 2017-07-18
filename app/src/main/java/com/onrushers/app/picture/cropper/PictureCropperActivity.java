package com.onrushers.app.picture.cropper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.onrushers.app.R;
import com.onrushers.app.common.activities.BaseActivity;

import java.io.File;

public class PictureCropperActivity extends BaseActivity
	implements PictureCropperFragment.Listener {

	public static final String EXTRA_PICTURE_PATH = "extra_picture_path";
	public static final String EXTRA_CROPPED_PATH = "extra_cropped_path";
	public static final String EXTRA_CROP_TYPE    = "extra_crop_type";

	private PictureCropperFragment mCropperFragment;

	//region Activity life cycle
	//---------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_cropper);
		Toolbar toolbar = (Toolbar) findViewById(R.id.picture_cropper_toolbar);
		setSupportActionBar(toolbar);

		if (getIntent().hasExtra(EXTRA_PICTURE_PATH)) {
			mCropperFragment = PictureCropperFragment.newInstance(
				getIntent().getStringExtra(EXTRA_PICTURE_PATH),
				getIntent().getStringExtra(EXTRA_CROP_TYPE));

			mCropperFragment.setListener(this);

			replaceCurrentFragment(mCropperFragment, PictureCropperFragment.TAG);
		}
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		replaceFragment(R.id.picture_cropper_content, fragment, tag);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mCropperFragment != null) {
			mCropperFragment.setListener(null);
		}
	}

	//---------------------------------------------------------------------------------------
	//endregion

	//region PictureCropperFragment.Listener
	//---------------------------------------------------------------------------------------

	@Override
	public void onCropPicture(File croppedFile) {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(EXTRA_CROPPED_PATH, croppedFile.getAbsolutePath());

		setResult(RESULT_OK, resultIntent);
		finish();
	}

	//---------------------------------------------------------------------------------------
	//endregion
}
