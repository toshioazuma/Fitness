package com.onrushers.app.settings.social_account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.onrushers.app.OnRushersMainActivity;
import com.onrushers.app.R;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.HasComponent;
import com.onrushers.app.internal.di.components.MainComponent;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SocialAccountActivity extends BaseActivity implements HasComponent<MainComponent> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_account);

		Toolbar toolbar = (Toolbar) findViewById(R.id.social_account_toolbar);
		setSupportActionBar(toolbar);
		ButterKnife.bind(this);

		replaceCurrentFragment(new SocialAccountFragment(), SocialAccountFragment.TAG);
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		replaceFragment(R.id.social_account_content_frame, fragment, tag);
	}

	@Override
	public MainComponent getComponent() {
		return OnRushersMainActivity.sharedInstance.getComponent();
	}

	@OnClick(R.id.social_account_back_button)
	public void onBack() {
		finish();
	}
}
