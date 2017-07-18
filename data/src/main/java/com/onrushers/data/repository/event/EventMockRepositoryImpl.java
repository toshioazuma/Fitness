package com.onrushers.data.repository.event;

import com.onrushers.data.models.Event;
import com.onrushers.data.models.Pagination;
import com.onrushers.domain.boundaries.EventRepository;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IEventRegisterResult;
import com.onrushers.domain.business.model.IPagination;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class EventMockRepositoryImpl implements EventRepository {

	@Inject
	public EventMockRepositoryImpl() {

	}

	@Override
	public Observable<IPagination> getEvents(int page, int count, String accessToken) {
		return Observable.just((IPagination) getMockEventsPagination(false));
	}

	@Override
	public Observable<IPagination> getMyEvents(int page, int count, String accessToken) {
		return Observable.just((IPagination) getMockEventsPagination(true));
	}

	@Override
	public Observable<IEvent> getEvent(Integer eventId, String accessToken) {
		return null;
	}

	@Override
	public Observable<IEventRegisterResult> registerToEventIndividually(String email, Integer userId, Integer eventId, String accessToken) {
		return null;
	}

	private IPagination<IEvent> getMockEventsPagination(boolean isMine) {
		List<Event> list = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			Event e = new Event();
			e.id = i + 1;
			if (i % 2 == 0) {
				e.title = "The Mud Day";
				e.description = "Lorem ipsum";
			} else {
				e.title = "42km";
				e.description = "Ipsum lorem";
			}

			Date date = new Date();
			date.setTime(System.currentTimeMillis() + (new Random().nextInt(2000) + 1000));
			e.date = date;
			e.isMine = isMine;

			e.placesMax = new Random().nextInt(100) + 20;
			e.placesLeft = (int) (i % 2 == 0 ? 0.8 : 1) * e.placesMax;
			e.price = (i % 2 != 0 ? null : new Random().nextDouble() * 150);
			e.publicGender = new Random().nextInt(3);
			e.organizerWord = "This is Tiësto";
			//e.recommendedLevel = "Beginner";

			list.add(e);
		}

		Pagination<Event> mockEvents = new Pagination<>();
		mockEvents.pages = 1;
		mockEvents.list = list;
		mockEvents.count = list.size();
		return mockEvents;
	}
}
