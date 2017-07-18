package com.onrushers.app.event.home;

import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.ISearchResult;

import java.util.List;

public interface EventsView {

	/**
	 * @param futureEvents
	 * @param isSearching  @deprecated
	 */
	void showFutureEvents(List<IEvent> futureEvents, boolean isSearching);

	/**
	 * @param myEvents
	 * @param isSearching @deprecated
	 */
	void showMyEvents(List<IEvent> myEvents, boolean isSearching);

	/**
	 * @param results
	 */
	void showResultEvents(List<ISearchResult> results);
}
