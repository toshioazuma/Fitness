package com.onrushers.data.repository.report;

import com.onrushers.data.api.service.ReportService;
import com.onrushers.data.models.FeedReport;
import com.onrushers.domain.boundaries.ReportRepository;
import com.onrushers.domain.business.model.IFeedReport;

import javax.inject.Inject;

import rx.Observable;

public class ReportApiRepositoryImpl implements ReportRepository {

	private final ReportService mReportService;

	@Inject
	public ReportApiRepositoryImpl(ReportService reportService) {
		mReportService = reportService;
	}

	@Override
	public Observable<IFeedReport> createFeedReport(Integer feedId, String accessToken) {

		FeedReport feedReport = new FeedReport();
		feedReport.feedId = feedId;

		return mReportService
			.postFeedReport(feedReport, accessToken)
			.cast(IFeedReport.class);
	}
}
