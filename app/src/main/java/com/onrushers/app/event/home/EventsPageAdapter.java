package com.onrushers.app.event.home;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.onrushers.app.R;
import com.onrushers.app.event.home.tabs.future.EventsTabFutureFragment;
import com.onrushers.app.event.home.tabs.future.EventsTabFutureView;
import com.onrushers.app.event.home.tabs.mine.EventsTabMineFragment;
import com.onrushers.app.event.home.tabs.mine.EventsTabMineView;
import com.onrushers.domain.business.model.IEvent;

import java.util.List;

public class EventsPageAdapter extends FragmentStatePagerAdapter {

	public static final int INDEX_FUTURE_EVENTS = 0;
	public static final int INDEX_MY_EVENTS     = 1;

	private final Context mContext;

	private EventsTabFutureView mFutureView;
	private EventsTabMineView   mMineView;

	public EventsPageAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}

	public void setFutureEvents(List<IEvent> events, boolean isSearching) {
		mFutureView.showEvents(events, isSearching);
	}

	public void setMyEvents(List<IEvent> events, boolean isSearching) {
		mMineView.showEvents(events, isSearching);
	}

	@Override
	public Fragment getItem(int position) {
		if (INDEX_FUTURE_EVENTS == position) {
			if (mFutureView == null) {
				mFutureView = new EventsTabFutureFragment();
			}
			return (Fragment) mFutureView;
		} else if (INDEX_MY_EVENTS == position) {
			if (mMineView == null) {
				mMineView = new EventsTabMineFragment();
			}
			return (Fragment) mMineView;
		}
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Resources res = mContext.getResources();
		if (INDEX_FUTURE_EVENTS == position) {
			return res.getString(R.string.events_tab_future_title);
		} else if (INDEX_MY_EVENTS == position) {
			return res.getString(R.string.events_tab_mine_title);
		}
		return null;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
