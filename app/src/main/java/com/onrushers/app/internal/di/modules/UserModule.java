package com.onrushers.app.internal.di.modules;

import com.onrushers.app.file.FileClient;
import com.onrushers.app.file.impl.FileClientImpl;
import com.onrushers.app.internal.di.PerActivity;
import com.onrushers.app.user.edit.UserEditPresenter;
import com.onrushers.app.user.edit.impl.UserEditPresenterImpl;
import com.onrushers.app.user.picker.UserPickerPresenter;
import com.onrushers.app.user.picker.impl.UserPickerPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

	public UserModule() {

	}

	@Provides
	@PerActivity
	FileClient provideFileClient(FileClientImpl fileClient) {
		return fileClient;
	}

	@Provides
	@PerActivity
	UserEditPresenter provideUserEditPresenter(UserEditPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public UserPickerPresenter provideUserPickerPresenter(UserPickerPresenterImpl presenter) {
		return presenter;
	}
}
