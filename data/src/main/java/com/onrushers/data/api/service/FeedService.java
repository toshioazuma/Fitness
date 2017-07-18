package com.onrushers.data.api.service;

import com.onrushers.data.models.Feed;
import com.onrushers.data.models.GenericResult;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface FeedService {

	@POST("api/feeds")
	Observable<Feed> postFeed(@Body Feed feed, @Query("access_token") String accessToken);

	@GET("api/feeds/{id}")
	Observable<Feed> getFeed(@Path("id") int feedId, @Query("access_token") String accessToken);

	@DELETE("api/feeds/{id}")
	Observable<GenericResult> deleteFeed(@Path("id") int feedId, @Query("access_token") String accessToken);

}
