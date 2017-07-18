package com.onrushers.app.feed.detail;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.HasComponent;
import com.onrushers.app.internal.di.components.DaggerFeedComponent;
import com.onrushers.app.internal.di.components.FeedComponent;
import com.onrushers.domain.business.model.IFeed;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedDetailActivity extends BaseActivity implements HasComponent<FeedComponent>, OnFeedDetailReadyListener {

	public static final String TAG = "FeedDetailA";

	private FeedDetailView mView;
	private FeedComponent  mComponent;

	//region Activity life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_detail);
		Toolbar toolbar = (Toolbar) findViewById(R.id.feed_detail_toolbar);
		setSupportActionBar(toolbar);
		ButterKnife.bind(this);

		if (getIntent().hasExtra(Extra.FEED)) {
			IFeed feed = getIntent().getParcelableExtra(Extra.FEED);
			replaceCurrentFragment(FeedDetailFragment.newInstance(feed), FeedDetailFragment.TAG);
		}

		if (getIntent().getBooleanExtra(Extra.FROM_SQUARE_PICTURE, false)) {
			if (Build.VERSION.SDK_INT >= 21) {
				postponeEnterTransition();
			} else {
				supportPostponeEnterTransition();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_feed_detail, menu);

		Resources r = getResources();
		int paddingRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());

		ImageButton optionsButton = (ImageButton) menu.findItem(R.id.action_feed_detail_options).getActionView();
		optionsButton.setBackgroundColor(Color.TRANSPARENT);
		optionsButton.setImageResource(R.drawable.ic_more_vert_white);
		optionsButton.setPadding(0, 0, paddingRight, 0);
		optionsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mView.showOptions(view);
			}
		});

		return true;
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);

		if (fragment instanceof FeedDetailView) {
			mView = (FeedDetailView) fragment;
			mView.setOnFeedDetailReadyListener(this);
		}
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		replaceFragment(R.id.feed_detail_content_frame, fragment, tag);
	}

	@Override public void onBackPressed() {
		if (getIntent().getBooleanExtra(Extra.FROM_SQUARE_PICTURE, false)) {
			supportFinishAfterTransition();
		} else {
			finish();
		}
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
				.feedModule(getFeedModule())
				.build();
		}
		return mComponent;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnFeedDetailReadyListener
	//----------------------------------------------------------------------------------------------

	@Override public void onReadyTransitionView(final View transitionView) {

		if (transitionView != null) {
			transitionView.getViewTreeObserver().addOnPreDrawListener(
				new ViewTreeObserver.OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						transitionView.getViewTreeObserver().removeOnPreDrawListener(this);

						if (Build.VERSION.SDK_INT >= 21) {
							startPostponedEnterTransition();
						} else {
							supportStartPostponedEnterTransition();
						}

						return true;
					}
				});
		} else {
			if (Build.VERSION.SDK_INT >= 21) {
				startPostponedEnterTransition();
			} else {
				supportStartPostponedEnterTransition();
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnClick
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.feed_detail_back_button)
	public void onBackButtonClick() {
		onBackPressed();
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
