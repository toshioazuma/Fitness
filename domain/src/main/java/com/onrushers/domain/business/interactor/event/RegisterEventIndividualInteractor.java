package com.onrushers.domain.business.interactor.event;

import com.onrushers.domain.business.interactor.Interactor;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IUser;

public interface RegisterEventIndividualInteractor extends Interactor {

	void setEmail(String email);

	void setEvent(IEvent event);
}
