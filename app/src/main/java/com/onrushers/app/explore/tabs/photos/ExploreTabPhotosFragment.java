package com.onrushers.app.explore.tabs.photos;

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
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.feed.adapters.FeedsPhotosAdapter;
import com.onrushers.app.feed.detail.FeedDetailActivity;
import com.onrushers.app.feed.detail.OnFeedDetailListener;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.common.widgets.recyclerview.EndlessRecyclerViewScrollListener;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.type.FeedType;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreTabPhotosFragment extends BaseFragment
	implements ExploreTabPhotosView, OnFeedDetailListener {

	public static final String TAG = "ExploreTabPhotosF";

	@Bind(R.id.explore_photos_recycler)
	RecyclerView mRecyclerView;

	@Inject
	ExploreTabPhotosPresenter mPresenter;

	FeedsPhotosAdapter mAdapter;

	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(MainComponent.class).inject(this);
		mPresenter.setView(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_explore_tab_photos, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mAdapter = new FeedsPhotosAdapter(this);
		mRecyclerView.setAdapter(mAdapter);

		GridLayoutManager layoutManager =
			new GridLayoutManager(getContext(), Constant.GRID_PHOTO_COLUMN_COUNT);
		mRecyclerView.setLayoutManager(layoutManager);

		mRecyclerView.addOnScrollListener(
			new EndlessRecyclerViewScrollListener(layoutManager) {
				@Override
				public void onLoadMore(int page, int totalItemsCount) {
					mPresenter.getPhotosAtPage(page);
				}
			});

		mPresenter.onViewCreated();
	}

	@Override public void onDestroyView() {
		super.onDestroyView();
		mRecyclerView.setAdapter(null);
	}

	@Override
	public void onDestroy() {
		mPresenter.onDestroy();
		super.onDestroy();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region ExploreTabPhotosView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showFeeds(List<IFeed> feeds, int page) {
		mAdapter.appendFeeds(feeds, page);
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
}
