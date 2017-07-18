package com.onrushers.app.settings.edit_account;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.onrushers.app.OnRushersMainActivity;
import com.onrushers.app.R;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.HasComponent;
import com.onrushers.app.internal.di.components.MainComponent;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditAccountActivity extends BaseActivity implements HasComponent<MainComponent> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_account);

		Toolbar toolbar = (Toolbar) findViewById(R.id.edit_account_toolbar);
		setSupportActionBar(toolbar);
		ButterKnife.bind(this);

		replaceCurrentFragment(new EditAccountFragment(), EditAccountFragment.TAG);
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		replaceFragment(R.id.edit_account_content_frame, fragment, tag);
	}

	@Override
	public MainComponent getComponent() {
		return OnRushersMainActivity.sharedInstance.getComponent();
	}

	@OnClick(R.id.edit_account_back_button)
	public void onBack() {
		finish();
	}
}
