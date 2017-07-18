package com.onrushers.data.api.service;

import com.onrushers.data.models.Pagination;
import com.onrushers.data.models.ResultResultItem;
import com.onrushers.data.models.User;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface SearchService {

	@GET("api/searches/{slug}")
	Observable<Pagination<ResultResultItem>> getSearch(@Path("slug") String slug,
	                                                   @Query("type") String type,
	                                                   @Query("page") int page,
	                                                   @Query("count") int count,
	                                                   @Query("access_token") String accessToken);

	@GET("search/users/{slug}")
	Observable<User> searchUser(@Path("slug") String userSlug);
}
