package com.onrushers.data.api.service;

import com.onrushers.data.models.FeedReport;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ReportService {

	@POST("api/reports")
	Observable<FeedReport> postFeedReport(@Body FeedReport report,
	                                      @Query("access_token") String accessToken);

}
