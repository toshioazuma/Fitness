package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.IFeedReport;

import rx.Observable;

public interface ReportRepository {

	Observable<IFeedReport> createFeedReport(Integer feedId, String accessToken);
}
