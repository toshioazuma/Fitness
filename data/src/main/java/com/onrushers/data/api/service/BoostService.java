package com.onrushers.data.api.service;

import com.onrushers.data.models.Boost;
import com.onrushers.data.models.BoostPagination;
import com.onrushers.data.models.GenericResult;
import com.onrushers.data.models.Pagination;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface BoostService {

	@POST("api/boosts")
	Observable<Boost> postBoost(@Body Boost boost,
	                            @Query("access_token") String accessToken);

	@DELETE("api/boosts/{id}")
	Observable<GenericResult> deleteBoost(@Path("id") int boostId,
	                                      @Query("access_token") String accessToken);

	@GET("api/feeds/{id}/boosts")
	Observable<Pagination<Boost>> getFeedBoosts(@Path("id") int feedId,
	                                            @Query("page") int page,
	                                            @Query("count") int cosunt,
	                                            @Query("access_token") String accessToken);
}
