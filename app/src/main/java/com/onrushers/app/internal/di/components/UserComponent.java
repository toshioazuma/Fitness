package com.onrushers.app.internal.di.components;

import com.onrushers.app.internal.di.PerActivity;
import com.onrushers.app.internal.di.modules.ActivityModule;
import com.onrushers.app.internal.di.modules.UserModule;
import com.onrushers.app.user.edit.UserEditFragment;
import com.onrushers.app.user.picker.UserPickerFragment;
import com.onrushers.data.internal.di.modules.DataModule;
import com.onrushers.domain.internal.di.modules.DomainModule;

import dagger.Component;

@PerActivity
@Component(
	dependencies = ApplicationComponent.class,
	modules = {
		ActivityModule.class,
		UserModule.class,
		DomainModule.class,
		DataModule.class
	}
)
public interface UserComponent extends ActivityComponent {

	void inject(UserEditFragment userEditFragment);

	void inject(UserPickerFragment userPickerFragment);
}