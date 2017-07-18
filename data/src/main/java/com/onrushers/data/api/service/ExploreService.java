package com.onrushers.data.api.service;

import com.onrushers.data.models.Feed;
import com.onrushers.data.models.Pagination;
import com.onrushers.data.models.RankPagination;
import com.onrushers.data.models.User;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ExploreService {

	@GET("api/explore/photos")
	Observable<Pagination<Feed>> getPhotos(@Query("page") int page,
	                                       @Query("count") int count,
	                                       @Query("access_token") String accessToken);

	@GET("api/explore/rank")
	Observable<RankPagination> getRank(@Query("page") int page,
	                                   @Query("count") int count,
	                                   @Query("access_token") String accessToken);

}
