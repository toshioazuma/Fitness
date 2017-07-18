package com.onrushers.app.user.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.onrushers.app.user.tabs.feeds.UserTabFeedsFragment;
import com.onrushers.app.user.tabs.photos.UserTabPhotosFragment;
import com.onrushers.app.user.tabs.training.UserTabTrainingFragment;

public class UserTabsPagerAdapter extends FragmentStatePagerAdapter {

	private static final int INDEX_TAB_FEEDS    = 0;
	private static final int INDEX_TAB_PHOTOS   = 1;
	private static final int INDEX_TAB_TRAINING = 2;

	private final Integer mUserId;

	public UserTabsPagerAdapter(FragmentManager fm, Integer userId) {
		super(fm);
		mUserId = userId;
	}

	@Override
	public Fragment getItem(int position) {
		if (INDEX_TAB_FEEDS == position) {
			return UserTabFeedsFragment.newInstance(mUserId);
		} else if (INDEX_TAB_PHOTOS == position) {
			return UserTabPhotosFragment.newInstance(mUserId);
		} else if (INDEX_TAB_TRAINING == position) {
			return UserTabTrainingFragment.newInstance(mUserId);
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return null;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (object instanceof View) {
			container.removeView((View) object);
		}

		super.destroyItem(container, position, object);
	}
}
