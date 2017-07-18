package com.onrushers.app.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.bus.events.DeleteFeedEvent;
import com.onrushers.app.common.bus.events.ReloadFeedsEvent;
import com.onrushers.app.common.bus.events.UserUpdateEvent;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.feed.adapters.FeedsAdapter;
import com.onrushers.app.feed.adapters.OnFeedDeleteListener;
import com.onrushers.app.feed.adapters.OnFeedHeaderListener;
import com.onrushers.app.feed.adapters.OnFeedReportListener;
import com.onrushers.app.feed.adapters.OnFeedRushListener;
import com.onrushers.app.feed.adapters.OnFeedRushesViewListener;
import com.onrushers.app.feed.adapters.OnUserSuggestionListener;
import com.onrushers.app.feed.create.FeedCreateActivity;
import com.onrushers.app.feed.detail.FeedDetailActivity;
import com.onrushers.app.feed.detail.OnFeedDetailListener;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.app.picture.cropper.PictureCropperMode;
import com.onrushers.app.picture.gallery.PictureGalleryPageAdapter;
import com.onrushers.app.picture.picker.PicturePickerAgent;
import com.onrushers.app.picture.picker.PicturePickerAgentCallbacksImpl;
import com.onrushers.app.user.OnUserDetailListener;
import com.onrushers.app.user.UserProfileActivity;
import com.onrushers.app.user.list.UserListActivity;
import com.onrushers.app.user.list.UserListConfiguration;
import com.onrushers.common.utils.ToastUtils;
import com.onrushers.common.widgets.appbarlayout.AppBarStateChangeListener;
import com.onrushers.common.widgets.recyclerview.EndlessRecyclerViewScrollListener;
import com.onrushers.domain.bus.BusProvider;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements HomeView,
	OnFeedHeaderListener, OnFeedRushListener, OnFeedDetailListener, OnFeedReportListener, OnFeedDeleteListener, OnFeedRushesViewListener, OnUserDetailListener, OnUserSuggestionListener {

	public static final String TAG = "Home";

	@Bind(R.id.home_appbar_layout)
	AppBarLayout mAppBarLayout;

	@Bind(R.id.home_wall_recycler)
	RecyclerView mWallRecyclerView;

	@Bind(R.id.home_collapsing_toolbar)
	CollapsingToolbarLayout mCollapsingToolbarLayout;

	@Bind(R.id.home_top_viewpager)
	ViewPager mTopViewPager;

	@Bind(R.id.home_top_circleindicator)
	CircleIndicator mTopPagerIndicator;

	@Inject
	HomePresenter mPresenter;

	FeedsAdapter mFeedsAdapter;

	PicturePickerAgentCallbacksImpl mPicturePickerAgentListener;

	PictureGalleryPageAdapter mTopGalleryAdapter;


	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(MainComponent.class).inject(this);
		BusProvider.getInstance().register(this);
		mPresenter.setView(this);

		mPicturePickerAgentListener = new PicturePickerAgentCallbacksImpl(getContext()) {
			@Override
			public void onPicturePickerDidCropFile(File file, int tag) {

				Intent createFeedIntent = new Intent(getActivity(), FeedCreateActivity.class);
				createFeedIntent.putExtra(FeedCreateActivity.EXTRA_PICTURE_FILE, file);
				startActivity(createFeedIntent);
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		TypedValue typedValue = new TypedValue();
		int actionBarHeight = 0;

		if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
		}

		mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener(actionBarHeight) {
			@Override
			public void onStateChanged(AppBarLayout appBarLayout, State state) {
				if (getBaseActivity() == null) {
					return;
				}

				AppBarLayout mainAppBarLayout = getBaseActivity().getAppBarLayout();
				if (mainAppBarLayout == null) {
					return;
				}

				if (state == State.COLLAPSED || state == State.MIN_COLLAPSED) {
					mainAppBarLayout.setBackgroundColor(ContextCompat.getColor(getContext(),
						R.color.colorPrimary));
				} else {
					mainAppBarLayout.setBackgroundColor(ContextCompat.getColor(getContext(),
						R.color.transparent));
				}
			}
		});

		mTopGalleryAdapter = new PictureGalleryPageAdapter(getChildFragmentManager());
		mTopViewPager.setAdapter(mTopGalleryAdapter);
		mTopPagerIndicator.setViewPager(mTopViewPager);

		mFeedsAdapter = new FeedsAdapter(this, this, this, this, this, this, this, this);
		mWallRecyclerView.setAdapter(mFeedsAdapter);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
		mWallRecyclerView.setLayoutManager(linearLayoutManager);
		mWallRecyclerView.addOnScrollListener(
			new EndlessRecyclerViewScrollListener(linearLayoutManager) {
				@Override
				public void onLoadMore(int page, int totalItemsCount) {
					mPresenter.getFeedsAtPage(page);
				}
			});
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mTopGalleryAdapter != null && mTopGalleryAdapter.getCount() == 0) {
			mPresenter.getPhotosSlider();
		}
		if (mFeedsAdapter != null && mFeedsAdapter.getItemCount() == 0) {
			mPresenter.getFeedsAtPage(1);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (PicturePickerAgent.isPicturePickerSupported(requestCode)) {
			PicturePickerAgent.getAgent(this).handleActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		AppBarLayout mainAppBarLayout = getBaseActivity().getAppBarLayout();
		if (mainAppBarLayout != null) {
			mainAppBarLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
		}

		mWallRecyclerView.setAdapter(null);
	}

	@Override
	public void onDestroy() {
		mPresenter.onDestroy();
		super.onDestroy();
		BusProvider.getInstance().unregister(this);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region HomeView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showFeeds(List<IFeed> feeds, int page) {
		mFeedsAdapter.appendFeeds(feeds, page);
	}

	@Override
	public void reloadView() {
		mFeedsAdapter.notifyDataSetChanged();
	}

	@Override
	public void showReportSent(boolean sent) {
		if (sent) {
			ToastUtils.showText(getContext(), R.string.report_sent);
		} else {
			ToastUtils.showText(getContext(), R.string.report_error);
		}
	}

	@Override
	public void showError(String errorMessage) {
		if (getView() == null) {
			return;
		}
		Snackbar.make(getView(), errorMessage, Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void hideTopSlider() {
		mCollapsingToolbarLayout.setVisibility(View.GONE);
	}

	@Override
	public void showTopSlider(List<Integer> photos) {
		mCollapsingToolbarLayout.setVisibility(View.VISIBLE);
		mTopGalleryAdapter.setPicturesIds(photos);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscriber
	//----------------------------------------------------------------------------------------------

	@Subscribe
	public void onReloadFeeds(ReloadFeedsEvent event) {
		mPresenter.getFeedsAtPage(1);
	}

	@Subscribe
	public void onUserUpdate(UserUpdateEvent event) {
		mPresenter.getFeedsAtPage(1);
	}

	@Subscribe
	public void onDeleteFeedEvent(DeleteFeedEvent event) {
		mFeedsAdapter.removeFeedById(event.getFeedId());
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnFeedHeaderListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onShowUserProfilePage(IUser user) {
		Intent intent = new Intent(getActivity(), UserProfileActivity.class);
		intent.putExtra(Extra.USER, user);
		startActivity(intent);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnFeedRushListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onRushFeed(IFeed feed) {
		mPresenter.addRush(feed);
	}

	@Override
	public void onUnrushFeed(IFeed feed, IBoost boost) {
		mPresenter.removeRush(feed, boost);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnFeedRushesViewListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onViewRushesFeed(IFeed feed) {
		Intent intent = new Intent(getActivity(), UserListActivity.class);
		intent.putExtra(Extra.USER_LIST_CONFIGURATION, UserListConfiguration.getRushesFeedConfiguration(feed));
		startActivity(intent);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnFeedDetailListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onFeedDetail(IFeed feed, ImageView source) {
		Intent intent = new Intent(getActivity(), FeedDetailActivity.class);
		intent.putExtra(Extra.FEED, feed);

		if (Build.VERSION.SDK_INT >= 21 && source != null) {
			ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
				getActivity(), source, getString(R.string.transitions_picture_square_to_feed_detail));

			startActivity(intent, options.toBundle());
		} else {
			startActivity(intent);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnFeedReportListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onReportFeed(IFeed feed) {
		mPresenter.reportFeed(feed);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnFeedDeleteListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onDeleteFeed(IFeed feed) {
		mPresenter.deleteFeed(feed);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnUserDetailListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onUserDetail(IUser user) {
		Intent intent = new Intent(getActivity(), UserProfileActivity.class);
		intent.putExtra(Extra.USER, user);
		startActivity(intent);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnUserSuggestionListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onSkipHeroSuggestion(IFeed feed) {
		mFeedsAdapter.skipHeroSuggestion(feed);
	}

	@Override
	public void onFollowHeroSuggestion(IFeed feed) {
		mPresenter.followUser(feed);
		mFeedsAdapter.skipHeroSuggestion(feed);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnClick
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.home_add_post_fab)
	public void onAddPostClick() {
		PicturePickerAgent.getAgent(this)
			.setCallbacksListener(mPicturePickerAgentListener)
			.setCropMode(PictureCropperMode.SQUARE)
			.presentBottomSheetDialog();
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
