package com.onrushers.app.picture.gallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.onrushers.app.R;

import java.util.List;

public class PictureGalleryPageAdapter extends FragmentStatePagerAdapter {

	private List<Integer> mPicturesIds;

	public PictureGalleryPageAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setPicturesIds(List<Integer> picturesIds) {
		mPicturesIds = picturesIds;
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int position) {
		return PicturePageFragment.newInstance(mPicturesIds.get(position));
	}

	@Override
	public int getCount() {
		if (mPicturesIds != null) {
			return mPicturesIds.size();
		}
		return 0;
	}
}
