package com.onrushers.app.internal.di.components;

import android.content.Context;
import android.content.SharedPreferences;

import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.modules.ApplicationModule;
import com.onrushers.data.internal.di.modules.DataModule;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.internal.di.modules.DomainModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
		dependencies = {
				DomainModule.class,
				DataModule.class
		},
		modules = {
				ApplicationModule.class
		}
)
public interface ApplicationComponent {
	void inject(BaseActivity baseActivity);

	Context context();
	ThreadExecutor threadExecutor();
	PostExecutionThread postExecutionThread();

	SharedPreferences sharedPreferences();
}
