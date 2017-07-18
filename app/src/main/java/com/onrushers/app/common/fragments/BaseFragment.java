package com.onrushers.app.common.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.onrushers.app.R;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.HasComponent;

public abstract class BaseFragment extends Fragment {

	protected <C> C getComponent(Class<C> componentType) {
		return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
	}

	protected BaseActivity getBaseActivity() {
		if (getActivity() instanceof BaseActivity) {
			return (BaseActivity) getActivity();
		}
		return null;
	}

	//region Navigation
	//----------------------------------------------------------------------------------------------

	protected void addNavigationBackToToolbar(Toolbar toolbar) {
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onNavigationBack();
			}
		});
	}

	protected void onNavigationBack() {
		getActivity().onBackPressed();
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
