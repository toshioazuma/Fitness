package com.onrushers.app.user.picker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.onrushers.app.R;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.HasComponent;
import com.onrushers.app.internal.di.components.DaggerUserComponent;
import com.onrushers.app.internal.di.components.UserComponent;
import com.onrushers.app.internal.di.modules.UserModule;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserPickerActivity extends BaseActivity implements HasComponent<UserComponent> {

	public static final String TAG = "UserPickerA";

	public static final String EXTRA_SELECTED_USERS = "extra.selected_users";

	@Bind(R.id.user_picker_toolbar)
	Toolbar mToolbar;

	private UserPickerView mView;

	private UserComponent mComponent;

	//region Activity life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_picker);
		ButterKnife.bind(this);
		setSupportActionBar(mToolbar);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);

		if (fragment instanceof UserPickerView) {
			mView = (UserPickerView) fragment;
		}
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		/** no container */
	}

	@Override
	public void onBackPressed() {
		if (mView != null) {
			Intent resultIntent = new Intent();
			resultIntent.putParcelableArrayListExtra(EXTRA_SELECTED_USERS, mView.getSelectedUsers());
			setResult(RESULT_OK, resultIntent);
		} else {
			setResult(RESULT_CANCELED);
		}
		finish();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region HasComponent<UserComponent>
	//----------------------------------------------------------------------------------------------

	@Override
	public UserComponent getComponent() {
		if (mComponent == null) {
			mComponent = DaggerUserComponent.builder()
				.applicationComponent(getApplicationComponent())
				.activityModule(getActivityModule())
				.dataModule(getDataModule())
				.userModule(new UserModule())
				.build();
		}
		return mComponent;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnClick
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.user_picker_back_button)
	public void onBackButtonClick() {
		onBackPressed();
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
