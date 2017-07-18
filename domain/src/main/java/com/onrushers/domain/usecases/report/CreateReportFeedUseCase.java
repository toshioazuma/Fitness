package com.onrushers.domain.usecases.report;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.ReportRepository;
import com.onrushers.domain.business.interactor.report.CreateReportFeedInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IFeedReport;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class CreateReportFeedUseCase extends UseCase implements CreateReportFeedInteractor {

	private static final String TAG = "CreateReportFeedUC";

	private final AuthSessionRepository mAuthSessionRepository;
	private final ReportRepository      mReportRepository;

	private Integer mFeedId;

	@Inject
	public CreateReportFeedUseCase(AuthSessionRepository authSessionRepository,
	                               ReportRepository reportRepository,
	                               ThreadExecutor threadExecutor,
	                               PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mReportRepository = reportRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IFeedReport>>() {
				@Override
				public Observable<IFeedReport> call(IAuthSession authSession) {

					return mReportRepository.createFeedReport(mFeedId, authSession.getToken());
				}
			});
	}

	@Override
	public void setFeed(IFeed feed) {
		mFeedId = feed.getId();
	}
}
