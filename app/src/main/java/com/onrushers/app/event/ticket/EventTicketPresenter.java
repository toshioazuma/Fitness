package com.onrushers.app.event.ticket;

import com.onrushers.domain.business.model.IEvent;

public interface EventTicketPresenter {

	void setView(EventTicketView view);

	void presentEvent(IEvent event);
}
