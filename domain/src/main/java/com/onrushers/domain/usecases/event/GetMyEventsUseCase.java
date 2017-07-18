package com.onrushers.domain.usecases.event;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.EventRepository;
import com.onrushers.domain.business.interactor.event.GetMyEventsInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.PaginationUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetMyEventsUseCase extends PaginationUseCase implements GetMyEventsInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final EventRepository       mEventRepository;

	@Inject
	public GetMyEventsUseCase(AuthSessionRepository authSessionRepository,
	                          EventRepository eventRepository, ThreadExecutor threadExecutor,
	                          PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mEventRepository = eventRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IPagination>>() {
				@Override
				public Observable<IPagination> call(final IAuthSession authSession) {

					return mEventRepository
						.getMyEvents(getPage(), getPageCount(), authSession.getToken())
						.map(new Func1<IPagination, IPagination>() {
							@Override
							public IPagination call(IPagination pagination) {

								for (IEvent event : (List<IEvent>) pagination.getItems()) {
									event.compareWithUserId(authSession.getUserId());
								}

								return pagination;
							}
						});
				}
			});
	}
}
