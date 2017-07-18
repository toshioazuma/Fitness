package com.onrushers.app.launch.confirmation;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.activities.BaseActivity;

public class RegisterConfirmationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_confirmation);

		if (getIntent() != null && getIntent().hasExtra(Extra.USER_EMAIL)) {
			String email = getIntent().getStringExtra(Extra.USER_EMAIL);

			replaceCurrentFragment(
				RegisterConfirmationFragment.newInstance(email), RegisterConfirmationFragment.TAG);
		}
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		replaceFragment(R.id.register_confirmation_content_frame, fragment, tag);
	}
}
