package com.onrushers.app.launch.home;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.onrushers.app.R;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.LaunchComponent;
import com.onrushers.app.launch.login.LogInFragment;
import com.onrushers.app.launch.signin.SignInFragment;
import com.onrushers.common.utils.ToastUtils;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LaunchHomeFragment extends BaseFragment implements LaunchHomeView {

	public static final String TAG = "LaunchHome";

	@Bind(R.id.launch_home_videoview)
	ScalableVideoView mVideoView;

	@Inject
	LaunchHomePresenter mPresenter;

	private Callbacks       mListener;
	private CallbackManager mCallbackManager;


	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof Callbacks) {
			mListener = (Callbacks) context;
		} else {
			throw new RuntimeException(context.toString() +
				" must implement LaunchHomeFragment.Callbacks");
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(LaunchComponent.class).inject(this);
		mPresenter.setView(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_launch_home, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mCallbackManager = CallbackManager.Factory.create();

		LoginManager.getInstance()
			.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

				@Override
				public void onSuccess(LoginResult loginResult) {
					AccessToken accessToken = loginResult.getAccessToken();
					mPresenter.facebookLogin(accessToken.getUserId());
				}

				@Override
				public void onCancel() {
				}

				@Override
				public void onError(FacebookException error) {
					error.printStackTrace();
				}
			});

		playVideo();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (!mVideoView.isPlaying()) {
			playVideo();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (mCallbackManager != null) {
			mCallbackManager.onActivityResult(requestCode, resultCode, data);
		}

		/**
		 * TODO
		 * Handle error login from Facebook
		 */
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

	//region LaunchHomeView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showRegistrationPageWithFacebook(final String facebookId) {
		final BaseActivity parentActivity = (BaseActivity) getActivity();

		AccessToken accessToken = AccessToken.getCurrentAccessToken();

		if (accessToken == null) {

			parentActivity.replaceFragment(
				R.id.launch_content_frame, SignInFragment.newInstance(facebookId), SignInFragment.TAG);
			return;
		}

		GraphRequest request = GraphRequest.newMeRequest(
			accessToken,
			new GraphRequest.GraphJSONObjectCallback() {
				@Override
				public void onCompleted(JSONObject object, GraphResponse response) {

					String lastName = null;
					String firstName = null;
					String email = null;
					String birthDate = null;
					String gender = null;

					if (object != null) {
						try {
							if (object.has("last_name")) {
								lastName = object.getString("last_name");
							}
							if (object.has("first_name")) {
								firstName = object.getString("first_name");
							}
							if (object.has("email")) {
								email = object.getString("email");
							}
							if (object.has("birthdate")) {
								birthDate = object.getString("birthdate");
							}
							if (object.has("gender")) {
								gender = object.getString("gender");
							}
						} catch (JSONException ex) {
							ex.printStackTrace();
						}
					}

					SignInFragment signInFragment = SignInFragment.newInstance(
						facebookId, lastName, firstName, email, birthDate, gender);

					parentActivity.replaceFragment(
						R.id.launch_content_frame, signInFragment, SignInFragment.TAG);
				}
			});

		Bundle parameters = new Bundle();
		parameters.putString("fields", "id,name,last_name,first_name,email,gender,birthday,location");
		request.setParameters(parameters);
		request.executeAsync();

	}

	@Override
	public void showErrorMessage(String[] messages) {
		if (messages != null && messages.length > 0) {
			ToastUtils.showText(getContext(), messages[messages.length - 1]);
		} else {
			ToastUtils.showText(getContext(), R.string.launch_error_login_unknown_occured);
		}
	}

	@Override
	public void showHome() {
		mListener.onAuthenticatedUser();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Button click events
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.launch_home_facebook_button)
	public void onFacebookLoginClick() {

		if (AccessToken.getCurrentAccessToken() == null) {

			LoginManager.getInstance()
				.logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

		} else {

			AccessToken accessToken = AccessToken.getCurrentAccessToken();
			mPresenter.facebookLogin(accessToken.getUserId());
		}
	}

	@OnClick(R.id.launch_home_email_button)
	public void onEmailLoginClick() {

		final BaseActivity parentActivity = (BaseActivity) getActivity();

		parentActivity.replaceFragment(
			R.id.launch_content_frame, new LogInFragment(), LogInFragment.TAG);
	}

	@OnClick(R.id.launch_home_signin_button)
	public void onSignInClick() {

		final BaseActivity parentActivity = (BaseActivity) getActivity();

		parentActivity.replaceFragment(
			R.id.launch_content_frame, new SignInFragment(), SignInFragment.TAG);
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
