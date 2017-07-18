package com.onrushers.app.user.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.onrushers.app.OnRushersMainActivity;
import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.HasComponent;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.type.UserListType;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserListActivity extends BaseActivity implements HasComponent<MainComponent> {

	public static final String TAG = "UserListA";

	@Bind(R.id.user_list_toolbar)
	Toolbar mToolbar;

	@Bind(R.id.user_list_title_textview)
	TextView mTitleTextView;

	private UserListView mUserListView;


	//region Activity life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		ButterKnife.bind(this);
		setSupportActionBar(mToolbar);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);

		if (fragment instanceof UserListView) {
			mUserListView = (UserListView) fragment;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mUserListView != null && getIntent() != null && getIntent().hasExtra(Extra.USER_LIST_CONFIGURATION)) {
			UserListConfiguration configuration =
				getIntent().getParcelableExtra(Extra.USER_LIST_CONFIGURATION);
			mUserListView.setConfiguration(configuration);
		}
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {

	}

	@Override
	public void setTitle(int titleId) {
		mTitleTextView.setText(titleId);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	@Override
	public MainComponent getComponent() {
		return OnRushersMainActivity.sharedInstance.getComponent();
	}

	@OnClick(R.id.user_list_back_button)
	public void onBackButtonClick() {
		onBackPressed();
	}
}
