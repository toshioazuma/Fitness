package com.onrushers.app.launch.confirmation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.fragments.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegisterConfirmationFragment extends BaseFragment {

	public static final String TAG = "RegisterConfirmationF";

	@Bind(R.id.register_confirmation_email_textview)
	TextView mEmailTextView;

	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	public static RegisterConfirmationFragment newInstance(String email) {
		Bundle args = new Bundle();
		args.putString(Extra.USER_EMAIL, email);

		RegisterConfirmationFragment fragment = new RegisterConfirmationFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register_confirmation, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (getArguments() != null && getArguments().get(Extra.USER_EMAIL) != null) {
			String email = getArguments().getString(Extra.USER_EMAIL);
			mEmailTextView.setText(email);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	@OnClick(R.id.register_confirmation_ok_button)
	public void onOkClick() {
		getActivity().finish();
	}
}
