package com.onrushers.app.internal.di.modules;

import android.app.Activity;

import com.onrushers.app.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

	private final Activity mActivity;

	public ActivityModule(Activity activity) {
		mActivity = activity;
	}

	@Provides
	@PerActivity
	Activity activity() {
		return mActivity;
	}
}
