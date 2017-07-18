package com.onrushers.app.event.home;

public interface EventsPresenter {

	void onViewCreated();

	void setView(EventsView view);

	void setQuery(String query);
}
