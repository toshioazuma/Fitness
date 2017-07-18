package com.onrushers.app.user.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.onrushers.app.R;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.HasComponent;
import com.onrushers.app.internal.di.components.DaggerUserComponent;
import com.onrushers.app.internal.di.components.UserComponent;
import com.onrushers.app.internal.di.modules.UserModule;
import com.onrushers.domain.business.model.IUser;

public class UserEditActivity extends BaseActivity implements HasComponent<UserComponent> {

	public static final String TAG = "UserEdit";

	public static final String EXTRA_USER = "extra.user";

	private UserComponent mComponent;

	//region Activity life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_edit);
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		if (getIntent().hasExtra(EXTRA_USER) &&
			getIntent().getParcelableExtra(EXTRA_USER) instanceof IUser) {

			IUser user = getIntent().getParcelableExtra(EXTRA_USER);

			replaceCurrentFragment(UserEditFragment.newInstance(user), UserEditFragment.TAG);
		}
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		replaceFragment(R.id.user_edit_content_frame, fragment, tag);
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


}
