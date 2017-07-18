package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IEventRegisterResult;
import com.onrushers.domain.business.model.IPagination;

import rx.Observable;

public interface EventRepository {

	Observable<IPagination> getEvents(int page, int count, String accessToken);

	Observable<IPagination> getMyEvents(int page, int count, String accessToken);

	Observable<IEvent> getEvent(Integer eventId, String accessToken);

	Observable<IEventRegisterResult> registerToEventIndividually(String email, Integer userId, Integer eventId, String accessToken);
}
