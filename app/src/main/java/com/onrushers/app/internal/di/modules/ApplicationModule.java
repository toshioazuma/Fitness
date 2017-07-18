package com.onrushers.app.internal.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.onrushers.app.OnRushersApplication;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.executor.impl.JobExecutor;
import com.onrushers.domain.executor.impl.UIThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

	private final OnRushersApplication mApp;

	public ApplicationModule(OnRushersApplication application) {
		mApp = application;
	}

	@Provides
	@Singleton
	Context provideApplicationContext() {
		return mApp;
	}

	@Provides
	@Singleton
	ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
		return jobExecutor;
	}

	@Provides
	@Singleton
	PostExecutionThread providePostExecutionThread(UIThread uiThread) {
		return uiThread;
	}

	@Provides
	@Singleton
	SharedPreferences provideSharedPreferences(Context context) {
		final String name = "OnRushersPrefs";
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
}
