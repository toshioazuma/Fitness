package com.onrushers.app.launch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.onrushers.app.BuildConfig;
import com.onrushers.app.OnRushersMainActivity;
import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.HasComponent;
import com.onrushers.app.internal.di.components.DaggerLaunchComponent;
import com.onrushers.app.internal.di.components.LaunchComponent;
import com.onrushers.app.internal.di.modules.LaunchModule;
import com.onrushers.app.launch.confirmation.RegisterConfirmationActivity;
import com.onrushers.app.launch.home.LaunchHomeFragment;
import com.onrushers.app.launch.launch.LaunchFragment;
import com.onrushers.app.launch.login.LogInFragment;
import com.onrushers.app.launch.signin.SignInFragment;
import com.onrushers.data.internal.di.modules.DataModule;

public class LaunchActivity extends BaseActivity implements HasComponent<LaunchComponent>,
	LaunchFragment.Callbacks, LaunchHomeFragment.Callbacks,
	LogInFragment.Callbacks, SignInFragment.Callbacks {

	public static final String TAG = "LaunchActivity";

	private LaunchComponent mComponent;

	//region Activity life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		replaceFragment(R.id.launch_content_frame, fragment, tag);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region HasComponent<LaunchComponent>
	//----------------------------------------------------------------------------------------------

	@Override
	public LaunchComponent getComponent() {
		if (mComponent == null) {
			mComponent = DaggerLaunchComponent.builder()
				.applicationComponent(getApplicationComponent())
				.activityModule(getActivityModule())
				.launchModule(new LaunchModule())
				.dataModule(new DataModule(getBaseContext(), BuildConfig.BASE_API))
				.build();
		}
		return mComponent;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region LaunchFragment.Callbacks & LaunchHomeFragment.Callbacks & LogInFragment.Callbacks
	//----------------------------------------------------------------------------------------------

	@Override
	public void onAuthenticatedUser() {
		/**
		 * User did logged in
		 */
		Log.d(TAG, "-> show home page because user did logged-in");

		Intent homeIntent = new Intent(this, OnRushersMainActivity.class);
		startActivity(homeIntent);

		finish();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region SignInFragment.Callbacks
	//----------------------------------------------------------------------------------------------

	@Override
	public void onNewCreatedUser(String email, String password) {
		Intent confirmationIntent = new Intent(this, RegisterConfirmationActivity.class);
		confirmationIntent.putExtra(Extra.USER_EMAIL, email);
		startActivity(confirmationIntent);

		removeFragment(SignInFragment.TAG);
		replaceCurrentFragment(new LaunchHomeFragment(), LaunchHomeFragment.TAG);
		replaceCurrentFragment(new LogInFragment(), LogInFragment.TAG);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
