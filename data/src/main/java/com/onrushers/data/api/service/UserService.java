package com.onrushers.data.api.service;

import com.onrushers.data.models.FeedPagination;
import com.onrushers.data.models.Notification;
import com.onrushers.data.models.Pagination;
import com.onrushers.data.models.Relation;
import com.onrushers.data.models.User;
import com.onrushers.data.models.PostUserResult;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface UserService {

	@POST("register")
	Observable<PostUserResult> postUser(@Body User user);

	@GET("api/users/{id}")
	Observable<User> getUser(@Path("id") Integer userId,
	                         @Query("access_token") String accessToken);

	@GET("api/users/{ids}")
	Observable<ArrayList<User>> getUsers(@Path("ids") String userIds,
	                                     @Query("access_token") String accessToken);

	@GET("api/users/{id}/feeds")
	Observable<FeedPagination> getUserFeeds(@Path("id") Integer userId,
	                                        @Query("page") int page,
	                                        @Query("count") int count,
	                                        @Query("type") Integer type,
	                                        @Query("access_token") String accessToken);

	@GET("api/walls/{id}")
	Observable<FeedPagination> getUserWall(@Path("id") Integer userId,
	                                       @Query("page") int page,
	                                       @Query("count") int count,
	                                       @Query("access_token") String accessToken);

	@GET("api/users/{id}/heros")
	Observable<Pagination<Relation>> getUserHeros(@Path("id") Integer userId,
	                                              @Query("page") int page,
	                                              @Query("count") int count,
	                                              @Query("access_token") String accessToken);

	@GET("api/users/{id}/fans")
	Observable<Pagination<Relation>> getUserFans(@Path("id") Integer userId,
	                                             @Query("page") int page,
	                                             @Query("count") int count,
	                                             @Query("access_token") String accessToken);

	@PUT("api/user")
	Observable<User> putUser(@Body User user,
	                         @Query("access_token") String accessToken);


	@GET("api/users/{id}/notifications")
	Observable<Pagination<Notification>> getUserNotifications(@Path("id") Integer userId,
	                                                          @Query("page") int page,
	                                                          @Query("count") int count,
	                                                          @Query("access_token") String accessToken);

}
