package com.onrushers.domain.usecases.relation;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.RelationRepository;
import com.onrushers.domain.business.interactor.relation.CreateRelationInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IRelation;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class CreateRelationUseCase extends UseCase implements CreateRelationInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final RelationRepository    mRelationRepository;

	private Integer mHeroId;

	@Inject
	public CreateRelationUseCase(AuthSessionRepository authSessionRepository,
	                             RelationRepository relationRepository,
	                             ThreadExecutor threadExecutor,
	                             PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mRelationRepository = relationRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IRelation>>() {
				@Override
				public Observable<IRelation> call(IAuthSession authSession) {

					return mRelationRepository.createRelation(
						authSession.getUserId(), mHeroId, authSession.getToken());
				}
			});
	}

	@Override
	public void setHero(IUser hero) {
		mHeroId = hero.getId();
	}
}
