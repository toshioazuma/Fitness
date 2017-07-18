package com.onrushers.app.event.detail;

import com.onrushers.domain.business.model.IEvent;

public interface EventDetailPresenter {

	void setView(EventDetailView view);

	void presentEvent(IEvent event);

	void reloadView();

	IEvent getPresentedEvent();

	void onDestroyView();
}
