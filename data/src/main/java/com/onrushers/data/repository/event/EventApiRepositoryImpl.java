package com.onrushers.data.repository.event;

import com.onrushers.data.api.service.EventService;
import com.onrushers.data.models.EventRegister;
import com.onrushers.domain.boundaries.EventRepository;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IEventPagination;
import com.onrushers.domain.business.model.IEventRegisterResult;
import com.onrushers.domain.business.model.IPagination;

import javax.inject.Inject;

import rx.Observable;

public class EventApiRepositoryImpl implements EventRepository {

	private final EventService mEventService;

	@Inject
	public EventApiRepositoryImpl(EventService service) {
		mEventService = service;
	}

	@Override
	public Observable<IPagination> getEvents(int page, int count, String accessToken) {
		return mEventService
			.getEvents(page, count, accessToken)
			.cast(IPagination.class);
	}

	@Override
	public Observable<IPagination> getMyEvents(int page, int count, String accessToken) {
		return mEventService
			.getMyEvents(page, count, accessToken)
			.cast(IPagination.class);
	}

	@Override
	public Observable<IEvent> getEvent(Integer eventId, String accessToken) {
		return mEventService
			.getEvent(eventId, accessToken)
			.cast(IEvent.class);
	}

	@Override
	public Observable<IEventRegisterResult> registerToEventIndividually(String email, Integer userId, Integer eventId, String accessToken) {
		EventRegister eventRegister = new EventRegister();
		eventRegister.email = email;
		eventRegister.userId = userId;
		eventRegister.eventId = eventId;

		return mEventService
			.registerToEventIndividual(eventRegister, accessToken)
			.cast(IEventRegisterResult.class);
	}
}
