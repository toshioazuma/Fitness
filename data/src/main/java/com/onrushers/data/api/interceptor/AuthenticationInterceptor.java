package com.onrushers.data.api.interceptor;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

	public static final String TAG = "Auth...Interceptor";

	private final String mBasicUsername;
	private final String mBasicPassword;

	public AuthenticationInterceptor(String basicUsername, String basicPassword) {
		mBasicUsername = basicUsername;
		mBasicPassword = basicPassword;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request originalRequest = chain.request();

		final String credentials = mBasicUsername + ":" + mBasicPassword;
		final String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(),
				Base64.NO_WRAP);

		Request newRequest = originalRequest.newBuilder()
				.header("Authorization", "Basic " + base64EncodedCredentials)
				.build();

		return chain.proceed(newRequest);
	}
}
