package com.onrushers.domain.usecases.event;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.EventRepository;
import com.onrushers.domain.business.interactor.event.RegisterEventIndividualInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IEventRegisterResult;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class RegisterEventIndividualUseCase extends UseCase
	implements RegisterEventIndividualInteractor {

	private final AuthSessionRepository mAuthRepo;
	private final EventRepository       mEventRepo;

	private String  mEmail;
	private Integer mEventId;

	@Inject
	public RegisterEventIndividualUseCase(AuthSessionRepository authRepo, EventRepository eventRepo,
	                                      ThreadExecutor threadExecutor,
	                                      PostExecutionThread postExecutionThread) {
		super(threadExecutor, postExecutionThread);

		mAuthRepo = authRepo;
		mEventRepo = eventRepo;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		return mAuthRepo.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IEventRegisterResult>>() {
				@Override
				public Observable<IEventRegisterResult> call(IAuthSession authSession) {

					return mEventRepo.registerToEventIndividually(
						mEmail, authSession.getUserId(), mEventId, authSession.getToken());
				}
			});
	}

	@Override
	public void setEmail(String email) {
		mEmail = email;
	}

	@Override
	public void setEvent(IEvent event) {
		mEventId = event.getId();
	}
}
