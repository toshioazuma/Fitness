package com.onrushers.app.settings.social_account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.onrushers.app.R;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.MainComponent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialAccountFragment extends BaseFragment implements SocialAccountView, CompoundButton.OnCheckedChangeListener {

	public static final String TAG = "SocialAccountF";

	@Bind(R.id.social_account_facebook_toggle_button)
	ToggleButton mFacebookToggleButton;

	@Bind(R.id.social_account_facebook_username_label)
	TextView mFacebookLoggedAsLabel;

	@Inject
	SocialAccountPresenter mPresenter;

	private CallbackManager mCallbackManager;

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
		View view = inflater.inflate(R.layout.fragment_social_account, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mFacebookToggleButton.setOnCheckedChangeListener(this);

		mCallbackManager = CallbackManager.Factory.create();
		LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				performFetchFacebookUserId();
			}

			@Override
			public void onCancel() {
			}

			@Override
			public void onError(FacebookException error) {
				error.printStackTrace();
			}
		});

		mPresenter.onViewCreated();
	}

	@Override
	public void onDestroyView() {
		mPresenter.destroyView();
		super.onDestroyView();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region SocialAccountView
	//----------------------------------------------------------------------------------------------

	@Override
	public void switchOffFacebookProfile() {
		mFacebookToggleButton.setChecked(false);
	}

	@Override
	public void switchOnFacebookProfile(@Nullable SpannableString facebookUsername) {
		mFacebookToggleButton.setChecked(true);

		if (TextUtils.isEmpty(facebookUsername)) {
			mFacebookLoggedAsLabel.setVisibility(View.GONE);
		} else {
			mFacebookLoggedAsLabel.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void showFacebookUserName(SpannableString userNameSpan) {
		mFacebookLoggedAsLabel.setText(userNameSpan);
		mFacebookToggleButton.setChecked(userNameSpan != null);
	}

	@Override
	public void performFetchFacebookUserId() {

		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if (accessToken == null) {
			LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
		} else {
			mPresenter.linkFacebookProfile(accessToken.getUserId());
			fetchFacebookUserName();
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region CompoundButton.OnCheckedChangeListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			performFetchFacebookUserId();
		} else {
			mPresenter.unlinkFacebookProfile();
		}
	}

	private void fetchFacebookUserName() {

		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if (accessToken == null) {
			return;
		}

		GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
			@Override
			public void onCompleted(JSONObject object, GraphResponse response) {

				if (object != null && object.has("name")) {
					try {
						String facebookName = object.getString("name");
						mPresenter.presentFacebookUserName(getContext(), facebookName);
					} catch (JSONException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		Bundle parameters = new Bundle();
		parameters.putString("fields", "id,name,email");
		request.setParameters(parameters);
		request.executeAsync();
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
