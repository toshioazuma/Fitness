package com.onrushers.app.launch.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.onrushers.app.R;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.LaunchComponent;
import com.onrushers.app.launch.reset.ResetPasswordFragment;
import com.onrushers.common.utils.KeyboardUtils;
import com.onrushers.common.utils.ToastUtils;
import com.onrushers.common.widgets.ORTextInputEditText;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFragment extends BaseFragment implements LoginView, Validator.ValidationListener {

	public static final String TAG = "LogInFragment";

	private Callbacks mListener;

	@Bind(R.id.login_videoview)
	ScalableVideoView mVideoView;

	@Bind(R.id.log_in_username_input)
	@NotEmpty
	@Order(1)
	protected EditText mUsernameInput;

	@Bind(R.id.log_in_password_input)
	@NotEmpty
	@Order(2)
	protected EditText mPasswordInput;

	protected Validator mValidator;

	protected boolean mValidationSucceeded = false;

	@Inject
	LogInPresenter mPresenter;

	ProgressDialog mProgressDialog;

	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	public LogInFragment() {
		// Required empty public constructor
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof Callbacks) {
			mListener = (Callbacks) context;
		} else {
			throw new RuntimeException(context.toString() +
				" must implement LogInFragment.Callbacks");
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(LaunchComponent.class).inject(this);
		mPresenter.setView(this);

		mValidator = new Validator(this);
		mValidator.setValidationMode(Validator.Mode.IMMEDIATE);
		mValidator.setValidationListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		playVideo();

		/**
		 * Fix typeface of password inputs
		 */
		mPasswordInput.setTransformationMethod(new PasswordTransformationMethod());

		mPasswordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

				if (actionId == EditorInfo.IME_ACTION_DONE) {
					onLogInClick();
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPresenter.onDestroy();
	}

	private void playVideo() {
		try {
			mVideoView.setRawData(R.raw.launch_home_video);
			mVideoView.setVolume(0, 0);
			mVideoView.setLooping(true);
			mVideoView.prepare(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mVideoView.start();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region LogInView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showLoading() {
		mProgressDialog = ProgressDialog.show(getContext(), getString(R.string.dialog_loading_title),
			getString(R.string.dialog_loading_message), true);
	}

	@Override
	public void hideLoading() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void showLogInErrorMessage(String errorMessage) {
		ToastUtils.showText(getContext(), errorMessage);
	}

	@Override
	public void onSuccessfulLogIn() {

		if (mListener != null) {
			mListener.onAuthenticatedUser();
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Validation listener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onValidationSucceeded() {
		mValidationSucceeded = true;
	}

	@Override
	public void onValidationFailed(List<ValidationError> errors) {

		if (errors != null && !errors.isEmpty()) {
			mValidationSucceeded = false;
		} else {
			mValidationSucceeded = true;
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Button actions
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.log_in_action_back_button)
	public void onActionBackClick() {
		getActivity().onBackPressed();
	}

	@OnClick(R.id.log_in_button)
	public void onLogInClick() {
		KeyboardUtils.hide(getActivity());

		mValidator.validate();
		if (!mValidationSucceeded) {
			ToastUtils.showText(getContext(), R.string.log_in_validator_required_fields);
			return;
		}

		mPresenter.setUsername(mUsernameInput.getText().toString());
		mPresenter.setPassword(mPasswordInput.getText().toString());

		mPresenter.logIn();
	}

	@OnClick(R.id.log_in_forgot_button)
	public void onForgotPasswordClick() {

		getBaseActivity().replaceCurrentFragment(
			new ResetPasswordFragment(), ResetPasswordFragment.TAG);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Public callbacks
	//----------------------------------------------------------------------------------------------

	public interface Callbacks {

		void onAuthenticatedUser();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
