package com.onrushers.app.picture.picker;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class PicturePickerOptionItem {

	private int mId;
	private int mDrawableRes;
	private int mTitleRes;

	public PicturePickerOptionItem(int id, @DrawableRes int drawable, @StringRes int title) {
		mId = id;
		mDrawableRes = drawable;
		mTitleRes = title;
	}

	public int getId() {
		return mId;
	}

	public int getDrawableRes() {
		return mDrawableRes;
	}

	public int getTitleRes() {
		return mTitleRes;
	}
}
