package com.onrushers.app.event.register;

import com.onrushers.domain.business.model.IEvent;

public interface EventRegisterPresenter {

	void setView(EventRegisterView view);

	void onViewCreated();

	void presentEvent(IEvent event);

	void registerIndividual(String email);
}
