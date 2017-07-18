package com.onrushers.app.event.home.tabs;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.onrushers.app.event.detail.OnEventDetailListener;
import com.onrushers.app.file.FileClient;
import com.onrushers.domain.business.model.IEvent;

import java.util.ArrayList;
import java.util.List;

public class EventsTabAdapter extends RecyclerView.Adapter<EventViewHolder> {

	private final OnEventDetailListener mEventDetailListener;
	private final FragmentManager       mFragmentManager;

	private List<IEvent> mEvents = new ArrayList<>();

	public EventsTabAdapter(OnEventDetailListener eventDetailListener, FragmentManager fragmentManager) {
		mEventDetailListener = eventDetailListener;
		mFragmentManager = fragmentManager;
	}

	public void appendsEvents(List<IEvent> events, int page) {
		if (page == 1) {
			mEvents.clear();
		}
		mEvents.addAll(events);
		notifyDataSetChanged();
	}

	//region RecyclerView.Adapter
	//----------------------------------------------------------------------------------------------

	@Override
	public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return EventViewHolder.newInstance(parent, mEventDetailListener, mFragmentManager);
	}

	@Override
	public void onBindViewHolder(EventViewHolder holder, int position) {
		IEvent event = mEvents.get(position);
		holder.setEvent(event);
	}

	@Override
	public int getItemCount() {
		if (mEvents != null) {
			return mEvents.size();
		}
		return 0;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
