package com.onrushers.domain.business.interactor.event;

import com.onrushers.domain.business.interactor.Interactor;
import com.onrushers.domain.business.model.IEvent;

public interface GetEventInteractor extends Interactor {

	void setEvent(IEvent event);
}
