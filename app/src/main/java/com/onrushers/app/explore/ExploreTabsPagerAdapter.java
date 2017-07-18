package com.onrushers.app.explore;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.onrushers.app.R;
import com.onrushers.app.explore.tabs.explore.ExploreTabExploreFragment;
import com.onrushers.app.explore.tabs.photos.ExploreTabPhotosFragment;
import com.onrushers.app.explore.tabs.ranking.ExploreTabRankingFragment;

public class ExploreTabsPagerAdapter extends FragmentPagerAdapter {

	private static final int INDEX_TAB_PHOTOS  = 0;
	private static final int INDEX_TAB_RANKING = 1;

	public ExploreTabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (INDEX_TAB_PHOTOS == position) {
			return new ExploreTabPhotosFragment();
		} else if (INDEX_TAB_RANKING == position) {
			return new ExploreTabRankingFragment();
		}
		return new Fragment();
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return null;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public int getIconAt(int position) {
		if (INDEX_TAB_PHOTOS == position) {
			return R.drawable.ic_explore_tab_picture;
		} else if (INDEX_TAB_RANKING == position) {
			return R.drawable.ic_explore_tab_ranking;
		}
		return R.drawable.ic_explore_tab_ranking;
	}
}
