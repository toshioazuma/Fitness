package com.onrushers.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.onrushers.app.internal.di.components.ApplicationComponent;
import com.onrushers.app.internal.di.components.DaggerApplicationComponent;
import com.onrushers.app.internal.di.modules.ApplicationModule;
import com.onrushers.data.internal.di.modules.DataModule;
import com.onrushers.domain.internal.di.modules.DomainModule;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class OnRushersApplication extends Application {

	public static final String TAG = "OnRushers";

	private ApplicationComponent mApplicationComponent;

	@Override
	public void onCreate() {
		super.onCreate();
		initializeInjector();
		initializeThirdParts();
		initializeFonts();

	}

	private void initializeInjector() {
		final String baseAPI = BuildConfig.BASE_API;

		mApplicationComponent = DaggerApplicationComponent.builder()
				.applicationModule(new ApplicationModule(this))
				.domainModule(new DomainModule())
				.dataModule(new DataModule(this, baseAPI))
				.build();
	}

	private void initializeThirdParts() {

		FacebookSdk.sdkInitialize(getApplicationContext());
		FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
		AppEventsLogger.activateApp(this);

		Fabric.with(this, new Crashlytics(), new Answers());
	}

	private void initializeFonts() {

		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
			.setDefaultFontPath("fonts/EurostileRegular.ttf")
			.setFontAttrId(R.attr.fontPath)
			.build()
		);
	}

	public ApplicationComponent getApplicationComponent() {
		return mApplicationComponent;
	}
}
