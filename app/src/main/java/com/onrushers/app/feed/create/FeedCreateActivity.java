package com.onrushers.app.feed.create;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.onrushers.app.R;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.HasComponent;
import com.onrushers.app.internal.di.components.DaggerFeedComponent;
import com.onrushers.app.internal.di.components.FeedComponent;
import com.onrushers.app.internal.di.modules.FeedModule;

import java.io.File;
import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedCreateActivity extends BaseActivity implements HasComponent<FeedComponent> {

	public static final String TAG = "FeedCreateActivity";

	public static final String EXTRA_PICTURE_FILE = "extra.picture_file";

	private FeedComponent mComponent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_create);
		Toolbar toolbar = (Toolbar) findViewById(R.id.feed_create_toolbar);
		setSupportActionBar(toolbar);
		ButterKnife.bind(this);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);

		if (fragment instanceof FeedCreateView) {
			FeedCreateView view = (FeedCreateView) fragment;

			if (getIntent() != null && getIntent().hasExtra(EXTRA_PICTURE_FILE)) {
				Serializable serializable = getIntent().getSerializableExtra(EXTRA_PICTURE_FILE);
				if (serializable != null && serializable instanceof File) {
					File pictureFile = (File) serializable;
					view.setPicture(pictureFile);
				}
			}
		}
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		// no container
	}

	@Override
	public FeedComponent getComponent() {

		if (mComponent == null) {
			mComponent = DaggerFeedComponent.builder()
				.applicationComponent(getApplicationComponent())
				.activityModule(getActivityModule())
				.dataModule(getDataModule())
				.feedModule(new FeedModule())
				.build();
		}
		return mComponent;
	}

	@OnClick(R.id.feed_create_back_button)
	public void onBackButtonClick() {
		finish();
	}
}
