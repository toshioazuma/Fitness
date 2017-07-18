package com.onrushers.app.event.home.impl;

import android.text.TextUtils;

import com.onrushers.app.event.home.EventsPresenter;
import com.onrushers.app.event.home.EventsView;
import com.onrushers.domain.business.interactor.event.GetEventsInteractor;
import com.onrushers.domain.business.interactor.event.GetMyEventsInteractor;
import com.onrushers.domain.business.interactor.search.SearchInteractor;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.model.ISearchResult;
import com.onrushers.domain.business.type.Page;
import com.onrushers.domain.business.type.SearchResultType;
import com.onrushers.domain.common.DefaultSubscriber;

import javax.inject.Inject;

public class EventsPresenterImpl implements EventsPresenter {

	private final GetEventsInteractor   mEventsInteractor;
	private final GetMyEventsInteractor mMyEventsInteractor;
	private final SearchInteractor      mSearchInteractor;

	private static final int COUNT_EVENTS_PER_PAGE = 50;

	private EventsView mView;

	@Inject
	public EventsPresenterImpl(GetEventsInteractor eventsInteractor,
	                           GetMyEventsInteractor myEventsInteractor,
	                           SearchInteractor searchInteractor) {

		mEventsInteractor = eventsInteractor;
		mEventsInteractor.setPageCount(COUNT_EVENTS_PER_PAGE);

		mMyEventsInteractor = myEventsInteractor;
		mMyEventsInteractor.setPageCount(COUNT_EVENTS_PER_PAGE);

		mSearchInteractor = searchInteractor;
		mSearchInteractor.setPageCount(COUNT_EVENTS_PER_PAGE);
	}

	@Override
	public void onViewCreated() {
		mEventsInteractor.execute(new DefaultSubscriber<IPagination<IEvent>>() {

			@Override
			public void onNext(IPagination<IEvent> pagination) {
				if (pagination.getItems() == null) {
					return;
				}
				mView.showFutureEvents(pagination.getItems(), false);
			}
		});
		mMyEventsInteractor.execute(new DefaultSubscriber<IPagination<IEvent>>() {

			@Override
			public void onNext(IPagination<IEvent> pagination) {
				if (pagination.getItems() == null) {
					return;
				}
				mView.showMyEvents(pagination.getItems(), false);
			}
		});
	}

	@Override
	public void setView(EventsView view) {
		mView = view;
	}

	@Override
	public void setQuery(String query) {

		if (TextUtils.isEmpty(query)) {
			return;
		}

		mSearchInteractor.setType(SearchResultType.Event);
		mSearchInteractor.setPage(Page.Min.value());
		mSearchInteractor.setQuery(query);

		mSearchInteractor.execute(new DefaultSubscriber<IPagination<ISearchResult>>() {
			@Override
			public void onNext(IPagination<ISearchResult> pagination) {
				mView.showResultEvents(pagination.getItems());
			}
		});
	}

}
