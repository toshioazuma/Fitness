package com.onrushers.app.event.home.tabs.future;

import android.support.v4.app.Fragment;

import com.onrushers.app.R;
import com.onrushers.app.event.home.tabs.EventsTabBaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsTabFutureFragment extends EventsTabBaseFragment implements EventsTabFutureView {

	@Override
	public int getNoDataMessageResId(boolean isSearching) {
		if (isSearching) {
			return R.string.event_search_no_result;
		}
		return R.string.future_events_no_data_message;
	}
}
