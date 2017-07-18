package com.onrushers.data.api.service;

import com.onrushers.domain.business.model.ISearchPlaceResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GoogleService {

	@GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
	Observable<ISearchPlaceResult> searchPlaces(@Query("location") String location,
	                                            @Query("radius") int radius,
	                                            @Query("type") String type,
	                                            @Query("name") String name,
	                                            @Query("key") String apiKey);

}
