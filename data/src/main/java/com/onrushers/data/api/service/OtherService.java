package com.onrushers.data.api.service;

import com.onrushers.data.models.SliderResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface OtherService {

	@GET("api/slider")
	Observable<SliderResult> getPhotosSlider(@Query("access_token") String accessToken);
}
