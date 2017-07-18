package com.onrushers.app.internal.di.components;

import android.app.Activity;

import com.onrushers.app.internal.di.PerActivity;
import com.onrushers.app.internal.di.modules.ActivityModule;

import dagger.Component;

@PerActivity
@Component(
		dependencies = ApplicationComponent.class,
		modules = ActivityModule.class
)
public interface ActivityComponent {

	Activity activity();
}
