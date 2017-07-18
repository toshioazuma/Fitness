package com.onrushers.app.internal.di.modules;

import com.onrushers.app.internal.di.PerActivity;
import com.onrushers.app.launch.home.LaunchHomePresenter;
import com.onrushers.app.launch.home.impl.LaunchHomePresenterImpl;
import com.onrushers.app.launch.launch.LaunchPresenter;
import com.onrushers.app.launch.launch.impl.LaunchPresenterImpl;
import com.onrushers.app.launch.login.LogInPresenter;
import com.onrushers.app.launch.login.impl.LogInPresenterImpl;
import com.onrushers.app.launch.reset.ResetPasswordPresenter;
import com.onrushers.app.launch.reset.impl.ResetPasswordPresenterImpl;
import com.onrushers.app.launch.signin.SignInPresenter;
import com.onrushers.app.launch.signin.impl.SignInPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class LaunchModule {

	public LaunchModule() {
		// Default empty constructor
	}


	@Provides
	@PerActivity
	LaunchPresenter provideLaunchPresenter(LaunchPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	LaunchHomePresenter provideLaunchHomePresenter(LaunchHomePresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	LogInPresenter provideLogInPresenter(LogInPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	ResetPasswordPresenter provideResetPasswordPresenter(ResetPasswordPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	SignInPresenter provideSignInPresenter(SignInPresenterImpl presenter) {
		return presenter;
	}
}
