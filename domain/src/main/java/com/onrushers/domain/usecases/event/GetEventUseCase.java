package com.onrushers.domain.usecases.event;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.EventRepository;
import com.onrushers.domain.business.interactor.event.GetEventInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetEventUseCase extends UseCase implements GetEventInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final EventRepository       mEventRepository;

	private Integer mEventId;

	@Inject
	public GetEventUseCase(AuthSessionRepository authSessionRepository,
	                       EventRepository eventRepository, ThreadExecutor threadExecutor,
	                       PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mEventRepository = eventRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		return mAuthSessionRepository.getAuthSession()
				.flatMap(new Func1<IAuthSession, Observable<IEvent>>() {
					@Override
					public Observable<IEvent> call(final IAuthSession authSession) {
						return mEventRepository
							.getEvent(mEventId, authSession.getToken())
							.map(new Func1<IEvent, IEvent>() {
								@Override
								public IEvent call(IEvent event) {
									event.compareWithUserId(authSession.getUserId());
									return event;
								}
							});
					}
				});
	}

	@Override
	public void setEvent(IEvent event) {
		mEventId = event.getId();
	}
}
