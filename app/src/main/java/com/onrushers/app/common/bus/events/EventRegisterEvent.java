package com.onrushers.app.common.bus.events;

import com.onrushers.domain.business.model.IEvent;

public class EventRegisterEvent {

	private final IEvent mEvent;

	public EventRegisterEvent(IEvent event) {
		mEvent = event;
	}

	public IEvent getEvent() {
		return mEvent;
	}
}
