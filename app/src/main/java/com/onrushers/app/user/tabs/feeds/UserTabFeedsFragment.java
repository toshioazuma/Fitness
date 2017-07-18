package com.onrushers.app.user.tabs.feeds;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.bus.events.DeleteFeedEvent;
import com.onrushers.app.common.bus.events.UserUpdateEvent;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.feed.adapters.FeedsAdapter;
import com.onrushers.app.feed.adapters.OnFeedDeleteListener;
import com.onrushers.app.feed.adapters.OnFeedReportListener;
import com.onrushers.app.feed.adapters.OnFeedRushListener;
import com.onrushers.app.feed.adapters.OnFeedRushesViewListener;
import com.onrushers.app.feed.detail.FeedDetailActivity;
import com.onrushers.app.feed.detail.OnFeedDetailListener;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.app.user.OnUserDetailListener;
import com.onrushers.app.user.UserProfileActivity;
import com.onrushers.app.user.list.UserListActivity;
import com.onrushers.app.user.list.UserListConfiguration;
import com.onrushers.common.utils.ToastUtils;
import com.onrushers.common.widgets.recyclerview.EndlessRecyclerViewScrollListener;
import com.onrushers.domain.bus.BusProvider;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserTabFeedsFragment extends BaseFragment
	implements UserTabFeedsView, OnFeedDetailListener, OnFeedReportListener, OnFeedDeleteListener,
	OnFeedRushListener, OnFeedRushesViewListener, OnUserDetailListener {

	public static final String TAG = "UserTabFeedsF";

	@Bind(R.id.user_tab_feeds_recyclerview)
	protected RecyclerView mRecyclerView;

	@Inject
	UserTabFeedsPresenter mPresenter;

	private FeedsAdapter mAdapter;

	public static UserTabFeedsFragment newInstance(Integer userId) {

		Bundle args = new Bundle();
		if (userId != null) {
			args.putInt(Extra.USER_ID, userId);
		}

		UserTabFeedsFragment fragment = new UserTabFeedsFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(MainComponent.class).inject(this);
		BusProvider.getInstance().register(this);
		mPresenter.setView(this);
		mAdapter = new FeedsAdapter(null, this, this, this, this, this, this, null);

		if (getArguments() != null && getArguments().get(Extra.USER_ID) instanceof Integer) {
			int userId = getArguments().getInt(Extra.USER_ID);
			mPresenter.setUserId(userId);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_tab_feeds, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mRecyclerView.setAdapter(mAdapter);

		LinearLayoutManager linearLayoutManager =
			(LinearLayoutManager) mRecyclerView.getLayoutManager();

		mRecyclerView.addOnScrollListener(
			new EndlessRecyclerViewScrollListener(linearLayoutManager) {
				@Override
				public void onLoadMore(int page, int totalItemsCount) {
					mPresenter.getFeedsAtPage(page);
				}
			});

		mPresenter.getFeedsAtPage(1);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BusProvider.getInstance().unregister(this);
	}

	@Override
	public void showFeeds(List<IFeed> feedsList, int page) {
		mAdapter.appendFeeds(feedsList, page);
	}

	@Override
	public void reloadView() {
		mAdapter.notifyDataSetChanged();
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
		Snackbar.make(getView(), errorMessage, Snackbar.LENGTH_SHORT).show();
	}

	//region Subscriber
	//----------------------------------------------------------------------------------------------

	@Subscribe
	public void onUserUpdate(UserUpdateEvent event) {
		mPresenter.getFeedsAtPage(1);
	}

	@Subscribe
	public void onDeleteFeedEvent(DeleteFeedEvent event) {
		mAdapter.removeFeedById(event.getFeedId());
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

	//region OnFeedDeleteListener
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

	//region OnUserDetailListener
	//----------------------------------------------------------------------------------------------

	@Override public void onUserDetail(IUser user) {
		Intent intent = new Intent(getActivity(), UserProfileActivity.class);
		intent.putExtra(Extra.USER, user);
		startActivity(intent);
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
