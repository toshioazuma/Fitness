package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IEventPagination;

import java.util.List;

public class EventPagination implements IEventPagination {

	@Expose
	public int count;

	@Expose
	public int pages;

	@Expose
	@SerializedName("list")
	public List<Event> events;


	//region IFeedPagination
	//----------------------------------------------------------------------------------------------

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public int getPages() {
		return pages;
	}

	@Override
	public List<? extends IEvent> getItems() {
		return events;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
