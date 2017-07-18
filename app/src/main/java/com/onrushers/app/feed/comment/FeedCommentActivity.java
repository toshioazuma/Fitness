package com.onrushers.app.feed.comment;

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

public class FeedCommentActivity extends BaseActivity implements HasComponent<FeedComponent> {

	private static final String TAG        = "FeedCommentActivity";
	public static final  String EXTRA_FEED = "extra.feed";

	private IFeed           mFeed;
	private FeedCommentView mCommentView;
	private FeedComponent   mComponent;


	//region Activity life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_feed_comment);
		Toolbar toolbar = (Toolbar) findViewById(R.id.feed_comment_toolbar);
		setSupportActionBar(toolbar);

		if (getIntent().hasExtra(EXTRA_FEED)) {
			mFeed = getIntent().getParcelableExtra(EXTRA_FEED);
		}
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		// no container
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);

		if (fragment instanceof FeedCommentView) {
			mCommentView = (FeedCommentView) fragment;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCommentView.setFeed(mFeed);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region HasComponent<FeedComponent>
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
