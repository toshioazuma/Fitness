package com.onrushers.app.explore.tabs.ranking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.file.FileClient;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.app.user.OnUserDetailListener;
import com.onrushers.app.user.UserProfileActivity;
import com.onrushers.common.widgets.recyclerview.DividerItemDecoration;
import com.onrushers.domain.business.model.IUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreTabRankingFragment extends BaseFragment implements ExploreTabRankingView, AdapterView.OnItemClickListener {

	public static final String TAG = "ExploreTabRankingF";

	@Bind(R.id.explore_rank_listview)
	ListView mRankListView;

	@Bind(R.id.explore_rank_coming_soon_textview)
	TextView mComingSoonTextView;

	@Inject
	ExploreTabRankingPresenter mPresenter;

	ExploreTabRankingAdapter mAdapter;

	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(MainComponent.class).inject(this);
		mPresenter.setView(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_explore_tab_ranking, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mAdapter = new ExploreTabRankingAdapter(getContext());
		mRankListView.setAdapter(mAdapter);
		mRankListView.setOnItemClickListener(this);

		mPresenter.onViewCreated();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mPresenter.onDestroyView();
		mRankListView.setAdapter(null);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPresenter.setView(null);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region ExploreTabRankingView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showUsers(@NotNull List<? extends IUser> users, int page) {
		mAdapter.addUsers(users);

		mRankListView.setVisibility(View.VISIBLE);
		mComingSoonTextView.setVisibility(View.GONE);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region AdapterView.OnItemClickListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		IUser user = (IUser) mAdapter.getItem(i);
		Intent intent = new Intent(getActivity(), UserProfileActivity.class);
		intent.putExtra(Extra.USER, user);
		startActivity(intent);
	}

	@Override
	public void showNoRanking() {
		mRankListView.setVisibility(View.GONE);
		mComingSoonTextView.setVisibility(View.VISIBLE);
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
