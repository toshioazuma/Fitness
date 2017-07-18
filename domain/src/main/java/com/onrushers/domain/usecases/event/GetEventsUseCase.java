package com.onrushers.domain.usecases.event;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.EventRepository;
import com.onrushers.domain.business.interactor.event.GetEventsInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IEventPagination;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.PaginationUseCase;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetEventsUseCase extends PaginationUseCase implements GetEventsInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final EventRepository       mEventRepository;

	@Inject
	public GetEventsUseCase(AuthSessionRepository authSessionRepository,
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
					public Observable<IPagination> call(IAuthSession authSession) {

						return mEventRepository.getEvents(
								getPage(), getPageCount(), authSession.getToken());
					}
				});
	}
}
