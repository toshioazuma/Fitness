package com.onrushers.app.launch.reset;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onrushers.app.R;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.LaunchComponent;
import com.onrushers.common.utils.ToastUtils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends BaseFragment implements ResetPasswordView {

	public static final String TAG = "ResetPasswordF";

	@Bind(R.id.reset_password_input_layout)
	LinearLayout mInputLayout;

	@Bind(R.id.reset_password_username_input)
	EditText mUsernameInput;

	@Bind(R.id.reset_password_sent_layout)
	LinearLayout mSentLayout;

	@Bind(R.id.reset_password_sent_title_textview)
	TextView mSentTitleTextView;

	@Bind(R.id.reset_password_sent_username_textview)
	TextView mSentUsernameTextView;

	@Inject
	ResetPasswordPresenter mPresenter;


	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(LaunchComponent.class).inject(this);
		mPresenter.setView(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mUsernameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

				if (actionId == EditorInfo.IME_ACTION_DONE) {

					mPresenter.requestResetPassword(mUsernameInput.getText().toString());
					return true;
				}
				return false;
			}
		});
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region ResetPasswordView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showEmailSentPage(String subject, boolean isEmail) {

		if (isEmail) {
			mSentTitleTextView.setText(R.string.reset_password_sent_title_email);
		} else {
			mSentTitleTextView.setText(R.string.reset_password_sent_title_username);
		}
		mSentUsernameTextView.setText(subject);

		mInputLayout.setVisibility(View.GONE);
		mSentLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void showUserNotFoundError() {

		ToastUtils.showText(getContext(), R.string.reset_password_failure_user_not_found);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Button actions
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.reset_password_action_back_button)
	public void onActionBackClick() {

		getActivity().onBackPressed();
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
