package com.onrushers.app.event.home.tabs;

import com.onrushers.domain.business.model.IEvent;

import java.util.List;

public interface EventsTabView {

	void showEvents(List<IEvent> events, boolean isSearching);
}
