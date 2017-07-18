package com.onrushers.data.api.service;

import com.onrushers.data.models.GenericResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ResetPasswordService {

	@GET("resetting")
	Observable<GenericResult> resetPassword(@Query("username") String username);
}
