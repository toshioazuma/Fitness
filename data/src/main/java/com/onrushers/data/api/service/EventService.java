package com.onrushers.data.api.service;

import com.onrushers.data.models.Event;
import com.onrushers.data.models.EventRegister;
import com.onrushers.data.models.EventRegisterResult;
import com.onrushers.data.models.Pagination;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface EventService {

	@GET("api/events")
	Observable<Pagination<Event>> getEvents(@Query("page") int page,
	                                        @Query("count") int count,
	                                        @Query("access_token") String accessToken);

	@GET("/api/user/events")
	Observable<Pagination<Event>> getMyEvents(@Query("page") int page,
	                                          @Query("count") int count,
	                                          @Query("access_token") String accessToken);

	@GET("/api/events/{id}")
	Observable<Event> getEvent(@Path("id") int eventId,
	                           @Query("access_token") String accessToken);

	@POST("/api/events/individuals")
	Observable<EventRegisterResult> registerToEventIndividual(@Body EventRegister eventRegister,
	                                                          @Query("access_token") String accessToken);
}
