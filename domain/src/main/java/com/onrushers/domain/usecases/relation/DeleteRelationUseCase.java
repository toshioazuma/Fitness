package com.onrushers.domain.usecases.relation;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.RelationRepository;
import com.onrushers.domain.business.interactor.relation.DeleteRelationInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.business.model.IRelation;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class DeleteRelationUseCase extends UseCase implements DeleteRelationInteractor {

	private final AuthSessionRepository mAuthSessionRepo;
	private final RelationRepository    mRelationRepo;

	private Integer mRelationId;

	@Inject
	public DeleteRelationUseCase(AuthSessionRepository authSessionRepo,
	                             RelationRepository relationRepo,
	                             ThreadExecutor threadExecutor,
	                             PostExecutionThread postExecutionThread) {
		super(threadExecutor, postExecutionThread);
		mAuthSessionRepo = authSessionRepo;
		mRelationRepo = relationRepo;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		return mAuthSessionRepo.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IGenericResult>>() {
				
				@Override
				public Observable<IGenericResult> call(IAuthSession authSession) {
					return mRelationRepo.deleteRelation(mRelationId, authSession.getToken());
				}
			});
	}

	@Override
	public void setRelation(IRelation relation) {
		mRelationId = relation.getId();
	}
}
