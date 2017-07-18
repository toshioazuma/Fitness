package com.onrushers.app.user.tabs.training;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.MainComponent;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserTabTrainingFragment extends BaseFragment implements UserTabTrainingView {

	public static final String TAG = "UserTabTrainingF";

	@Bind(R.id.user_tab_training_rush_count_textview)
	TextView mRushCountTextView;

	@Inject
	UserTabTrainingPresenter mPresenter;


	//region Constructor
	//----------------------------------------------------------------------------------------------

	public static UserTabTrainingFragment newInstance(Integer userId) {
		Bundle args = new Bundle();
		if (userId != null) {
			args.putInt(Extra.USER_ID, userId);
		}

		UserTabTrainingFragment fragment = new UserTabTrainingFragment();
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
		mPresenter.setView(this);

		Bundle args = getArguments();
		if (args != null && args.get(Extra.USER_ID) != null) {
			mPresenter.setUserId(args.getInt(Extra.USER_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_tab_training, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPresenter.onViewCreated();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region UserTabTrainingView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showBoostCount(int boostCount) {
		mRushCountTextView.setText(String.valueOf(boostCount));
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
