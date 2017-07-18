package com.onrushers.app.explore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
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
import com.onrushers.app.file.FileClient;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.app.search.SearchAdapter;
import com.onrushers.app.user.OnUserDetailListener;
import com.onrushers.app.user.UserProfileActivity;
import com.onrushers.domain.business.model.ISearchResult;
import com.onrushers.domain.business.model.IUser;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class ExploreFragment extends BaseFragment implements ExploreView, OnUserDetailListener {

	public static final String TAG = "ExploreF";

	@Bind(R.id.explore_tabs_layout)
	TabLayout mTabsLayout;

	@Bind(R.id.explore_tabs_viewpager)
	ViewPager mTabsViewPager;

	@Bind(R.id.explore_search_result_recycler)
	RecyclerView mResultRecyclerView;

	@Inject
	ExplorePresenter mPresenter;

	ExploreTabsPagerAdapter mPagerAdapter;

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
		View view = inflater.inflate(R.layout.fragment_explore, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mSearchAdapter = new SearchAdapter(this);
		mResultRecyclerView.setAdapter(mSearchAdapter);

		mPagerAdapter = new ExploreTabsPagerAdapter(getChildFragmentManager());
		mTabsViewPager.setAdapter(mPagerAdapter);

		mTabsLayout.setupWithViewPager(mTabsViewPager);
		for (int i = 0; i < mTabsLayout.getTabCount(); i++) {
			mTabsLayout.getTabAt(i).setIcon(mPagerAdapter.getIconAt(i));
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
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		Toolbar parentToolbar = getBaseActivity().getToolbar();
		if (parentToolbar != null && parentToolbar.getMenu() != null) {
			parentToolbar.getMenu().clear();
		}
	}

	@Override
	public void onDestroy() {
		mPresenter.onDestroy();
		super.onDestroy();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region ExploreView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showResultUsers(List<ISearchResult> results) {
		mResultRecyclerView.setVisibility(View.VISIBLE);
		mSearchAdapter.setResultsList(results);
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
}
