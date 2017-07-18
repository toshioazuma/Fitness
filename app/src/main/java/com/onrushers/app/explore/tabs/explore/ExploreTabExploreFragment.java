package com.onrushers.app.explore.tabs.explore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onrushers.app.R;
import com.onrushers.app.common.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreTabExploreFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_explore_tab_explore, container, false);
	}
}
