package com.onrushers.app.event.home.tabs.mine;

import android.support.v4.app.Fragment;

import com.onrushers.app.R;
import com.onrushers.app.event.home.tabs.EventsTabBaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsTabMineFragment extends EventsTabBaseFragment implements EventsTabMineView {

	@Override
	public int getNoDataMessageResId(boolean isSearching) {
		if (isSearching) {
			return R.string.event_search_no_result;
		}
		return R.string.my_events_no_data_message;
	}
}
