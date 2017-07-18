package com.onrushers.app.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.onrushers.app.OnRushersMainActivity;
import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.HasComponent;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.domain.business.model.IUser;

public class UserProfileActivity extends BaseActivity implements HasComponent<MainComponent> {

	//region Activity life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);

		if (getIntent() != null && getIntent().hasExtra(Extra.USER)) {
			IUser user = getIntent().getParcelableExtra(Extra.USER);
			replaceCurrentFragment(
				UserProfileFragment.newInstance(user.getId()), UserProfileFragment.TAG);
		} else {
			replaceCurrentFragment(
				UserProfileFragment.newMyProfileFragment(), UserProfileFragment.TAG);
		}
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		replaceFragment(R.id.user_profile_content_frame, fragment, tag);
	}

	@Override
	public MainComponent getComponent() {
		return OnRushersMainActivity.sharedInstance.getComponent();
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
