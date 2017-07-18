package com.onrushers.data.api.interceptor;

import android.os.Handler;
import android.os.Looper;

import com.onrushers.domain.bus.BusProvider;
import com.onrushers.domain.bus.events.InvalidTokenEvent;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class InvalidTokenInterceptor implements Interceptor {

	public static final String TAG = "InvalidToken";

	@Override
	public Response intercept(Chain chain) throws IOException {

		Response response = chain.proceed(chain.request());
		boolean unauthorized = response.code() == 401;

		if (unauthorized) {

			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					BusProvider.getInstance().post(new InvalidTokenEvent());
				}
			});
		}

		return response;
	}
}
