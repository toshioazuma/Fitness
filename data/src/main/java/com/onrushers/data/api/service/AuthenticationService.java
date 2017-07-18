package com.onrushers.data.api.service;

import com.onrushers.data.models.Login;
import com.onrushers.data.models.PostLoginResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface AuthenticationService {

	@POST("login")
	Call<PostLoginResult> loginUser(@Body Login login);

}
