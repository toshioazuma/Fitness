package com.onrushers.app.feed.boost;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.onrushers.app.R;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.HasComponent;
import com.onrushers.app.internal.di.components.DaggerFeedComponent;
import com.onrushers.app.internal.di.components.FeedComponent;
import com.onrushers.app.internal.di.modules.FeedModule;
import com.onrushers.domain.business.model.IFeed;

public class FeedBoostListActivity extends BaseActivity implements HasComponent<FeedComponent> {

	public static final String EXTRA_FEED = "extra.feed";

	private IFeed             mFeed;
	private FeedBoostListView mBoostListView;
	private FeedComponent     mComponent;


	//region Activity life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_feed_boost_list);
		Toolbar toolbar = (Toolbar) findViewById(R.id.feed_boost_list_toolbar);
		setSupportActionBar(toolbar);

		if (getIntent().hasExtra(EXTRA_FEED)) {
			mFeed = getIntent().getParcelableExtra(EXTRA_FEED);
		}
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);

		if (fragment instanceof FeedBoostListView) {
			mBoostListView = (FeedBoostListView) fragment;
		}
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		// no container
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mBoostListView != null) {
			mBoostListView.setFeed(mFeed);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region FeedComponent
	//----------------------------------------------------------------------------------------------

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

	//----------------------------------------------------------------------------------------------
	//endregion
}
