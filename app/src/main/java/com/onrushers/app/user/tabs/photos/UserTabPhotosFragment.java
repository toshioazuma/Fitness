package com.onrushers.app.user.tabs.photos;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.onrushers.app.R;
import com.onrushers.app.common.Constant;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.bus.events.DeleteFeedEvent;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.feed.adapters.FeedsPhotosAdapter;
import com.onrushers.app.feed.detail.FeedDetailActivity;
import com.onrushers.app.feed.detail.OnFeedDetailListener;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.domain.bus.BusProvider;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.type.FeedType;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Class UserTabPhotosFragment
 * ====
 * A {@link Fragment} that show user picture feeds in the {@link com.onrushers.app.user.UserProfileFragment}.
 * This is the second tab in the user profile page.
 * <p/>
 * Picture feeds are POST type {@link com.onrushers.domain.business.type.FeedType}.
 * <p/>
 * Picture feeds are displayed in a grid of 3 columns.
 */
public class UserTabPhotosFragment extends BaseFragment implements UserTabPhotosView, OnFeedDetailListener {

	public static final String TAG = "UserTabPhotosF";

	@Bind(R.id.user_tab_photos_recyclerview)
	RecyclerView mRecyclerView;

	@Inject
	UserTabPhotosPresenter mPresenter;

	FeedsPhotosAdapter mAdapter;


	//region Constructor
	//----------------------------------------------------------------------------------------------

	public static UserTabPhotosFragment newInstance(Integer userId) {
		Bundle args = new Bundle();
		if (userId != null) {
			args.putInt(Extra.USER_ID, userId);
		}

		UserTabPhotosFragment fragment = new UserTabPhotosFragment();
		fragment.setArguments(args);
		return fragment;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(MainComponent.class).inject(this);
		BusProvider.getInstance().register(this);
		mPresenter.setView(this);

		mAdapter = new FeedsPhotosAdapter(this);

		if (getArguments() != null && getArguments().get(Extra.USER_ID) instanceof Integer) {
			int userId = getArguments().getInt(Extra.USER_ID);
			mPresenter.setUserId(userId);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_tab_photos, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mRecyclerView.setLayoutManager(
			new GridLayoutManager(getContext(), Constant.GRID_PHOTO_COLUMN_COUNT));
		mRecyclerView.setAdapter(mAdapter);

		mPresenter.onViewCreated();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BusProvider.getInstance().unregister(this);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region UserTabPhotosView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showFeeds(List<IFeed> feedsList, int page) {
		mAdapter.appendFeeds(feedsList, page);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnFeedDetailListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onFeedDetail(IFeed feed, ImageView source) {
		if (feed.getType() != FeedType.Post) {
			/** skip other type that is not a POST */
			return;
		}
		Intent intent = new Intent(getActivity(), FeedDetailActivity.class);
		intent.putExtra(Extra.FEED, feed);

		if (Build.VERSION.SDK_INT >= 21 && source != null) {
			intent.putExtra(Extra.FROM_SQUARE_PICTURE, true);

			ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
				getActivity(), source, getString(R.string.transitions_picture_square_to_feed_detail));

			startActivity(intent, options.toBundle());
		} else {
			startActivity(intent);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscriber
	//----------------------------------------------------------------------------------------------

	@Subscribe
	public void onDeleteFeedEvent(DeleteFeedEvent event) {
		mAdapter.removeFeedById(event.getFeedId());
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
