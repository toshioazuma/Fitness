package com.onrushers.app.event.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.event.detail.EventDetailActivity;
import com.onrushers.app.event.detail.OnEventDetailListener;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.app.search.SearchAdapter;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.ISearchResult;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends BaseFragment implements EventsView, OnEventDetailListener {

	public static final String TAG = "EventsF";

	@Bind(R.id.events_tabs)
	TabLayout mTabLayout;

	@Bind(R.id.events_pager)
	ViewPager mPager;

	@Bind(R.id.events_search_result_recycler)
	RecyclerView mResultRecyclerView;

	@Inject
	EventsPresenter mPresenter;

	EventsPageAdapter mPageAdapter;

	SearchAdapter mSearchAdapter;


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
		View view = inflater.inflate(R.layout.fragment_events, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mPageAdapter = new EventsPageAdapter(getContext(), getChildFragmentManager());
		mPager.setAdapter(mPageAdapter);

		mSearchAdapter = new SearchAdapter(this);
		mResultRecyclerView.setAdapter(mSearchAdapter);

		if (ViewCompat.isLaidOut(mTabLayout)) {
			mTabLayout.setupWithViewPager(mPager);
		} else {
			mTabLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
				@Override
				public void onLayoutChange(View v, int left, int top, int right, int bottom,
				                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
					mTabLayout.setupWithViewPager(mPager);
					mTabLayout.removeOnLayoutChangeListener(this);
				}
			});
		}

		Toolbar parentToolbar = getBaseActivity().getToolbar();
		if (parentToolbar != null) {
			parentToolbar.inflateMenu(R.menu.menu_search);

			MenuItem searchItem = parentToolbar.getMenu().findItem(R.id.search_item);
			final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
			searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
				@Override
				public boolean onQueryTextSubmit(String query) {
					searchView.clearFocus();
					return true;
				}

				@Override
				public boolean onQueryTextChange(String newText) {
					mPresenter.setQuery(newText);
					return false;
				}
			});
			searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean focus) {
					mResultRecyclerView.setVisibility(focus ? View.VISIBLE : View.GONE);

					AppBarLayout mainAppBarLayout = getBaseActivity().getAppBarLayout();
					if (focus) {
						mainAppBarLayout.setBackgroundColor(ContextCompat.getColor(getContext(),
							R.color.colorPrimary));
					} else {
						mainAppBarLayout.setBackgroundColor(ContextCompat.getColor(getContext(),
							R.color.transparent));
					}
				}
			});
		}

		mPresenter.onViewCreated();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		Toolbar parentToolbar = getBaseActivity().getToolbar();
		if (parentToolbar != null && parentToolbar.getMenu() != null) {
			parentToolbar.getMenu().clear();
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region EventsView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showFutureEvents(List<IEvent> futureEvents, boolean isSearching) {
		mPageAdapter.setFutureEvents(futureEvents, isSearching);
	}

	@Override
	public void showMyEvents(List<IEvent> myEvents, boolean isSearching) {
		mPageAdapter.setMyEvents(myEvents, isSearching);
	}

	@Override
	public void showResultEvents(List<ISearchResult> results) {
		mResultRecyclerView.setVisibility(View.VISIBLE);
		mSearchAdapter.setResultsList(results);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnEventDetailListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onEventDetail(IEvent event) {
		Intent intent = new Intent(getActivity(), EventDetailActivity.class);
		intent.putExtra(Extra.EVENT, event);
		startActivity(intent);
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
