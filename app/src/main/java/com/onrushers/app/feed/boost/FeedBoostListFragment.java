package com.onrushers.app.feed.boost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onrushers.app.R;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.FeedComponent;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IFeed;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedBoostListFragment extends BaseFragment implements FeedBoostListView {

	public static final String TAG = "FeedBoostListFr";

	@Bind(R.id.feed_boost_recycler)
	RecyclerView mBoostRecyclerView;

	@Inject
	FeedBoostListPresenter mPresenter;

	FeedBoostsAdapter mAdapter;


	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	public FeedBoostListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(FeedComponent.class).inject(this);
		mPresenter.setView(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_feed_boost_list, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mAdapter = new FeedBoostsAdapter();
		mBoostRecyclerView.setAdapter(mAdapter);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region FeedBoostListView
	//----------------------------------------------------------------------------------------------

	@Override
	public void setFeed(IFeed feed) {
		mPresenter.setFeed(feed);
		mPresenter.loadBoosts();
	}

	@Override
	public void showBoosts(List<IBoost> boosts, int page) {
		mAdapter.appendBoosts(boosts, page);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
