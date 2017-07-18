package com.onrushers.app.internal.di.components;

import com.onrushers.app.internal.di.PerActivity;
import com.onrushers.app.internal.di.modules.ActivityModule;
import com.onrushers.app.internal.di.modules.LaunchModule;
import com.onrushers.app.launch.home.LaunchHomeFragment;
import com.onrushers.app.launch.launch.LaunchFragment;
import com.onrushers.app.launch.login.LogInFragment;
import com.onrushers.app.launch.reset.ResetPasswordFragment;
import com.onrushers.app.launch.signin.SignInFragment;
import com.onrushers.data.internal.di.modules.DataModule;
import com.onrushers.domain.internal.di.modules.DomainModule;

import dagger.Component;

@PerActivity
@Component(
		dependencies = ApplicationComponent.class,
		modules = {
				ActivityModule.class,
				LaunchModule.class,
				DomainModule.class,
				DataModule.class
		}
)
public interface LaunchComponent extends ActivityComponent {

	void inject(LaunchFragment launchFragment);

	void inject(LaunchHomeFragment launchHomeFragment);

	void inject(LogInFragment logInFragment);

	void inject(ResetPasswordFragment resetPasswordFragment);

	void inject(SignInFragment signInFragment);
}
