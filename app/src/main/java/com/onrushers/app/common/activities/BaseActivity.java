package com.onrushers.app.common.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.onrushers.app.BuildConfig;
import com.onrushers.app.OnRushersApplication;
import com.onrushers.app.internal.di.components.ApplicationComponent;
import com.onrushers.app.internal.di.modules.ActivityModule;
import com.onrushers.app.internal.di.modules.FeedModule;
import com.onrushers.app.internal.di.modules.MainModule;
import com.onrushers.data.internal.di.modules.DataModule;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends com.onrushers.common.activities.BaseActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getApplicationComponent().inject(this);
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	protected ApplicationComponent getApplicationComponent() {
		return ((OnRushersApplication) getApplication()).getApplicationComponent();
	}

	protected ActivityModule getActivityModule() {
		return new ActivityModule(this);
	}

	protected DataModule getDataModule() {
		return new DataModule(getBaseContext(), BuildConfig.BASE_API);
	}

	protected MainModule getMainModule() {
		return new MainModule();
	}

	protected FeedModule getFeedModule() {
		return new FeedModule();
	}

	public abstract void replaceCurrentFragment(Fragment fragment, String tag);

	public AppBarLayout getAppBarLayout() {
		return null;
	}

	public Toolbar getToolbar() {
		return null;
	}

	public RelativeLayout getContainerLayout() {
		return null;
	}
}