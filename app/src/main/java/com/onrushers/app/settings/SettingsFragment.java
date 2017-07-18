package com.onrushers.app.settings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onrushers.app.BuildConfig;
import com.onrushers.app.R;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.app.settings.edit_account.EditAccountActivity;
import com.onrushers.app.settings.edit_account.EditAccountFragment;
import com.onrushers.app.settings.social_account.SocialAccountActivity;
import com.onrushers.app.settings.social_account.SocialAccountFragment;
import com.onrushers.app.settings.website.WebsiteActivity;
import com.onrushers.common.utils.ToastUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.cketti.mailto.EmailIntentBuilder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment implements SettingsView {

	public static final String TAG = "SettingsF";

	private Integer mUserId;

	@Inject
	SettingsPresenter mPresenter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(MainComponent.class).inject(this);
		mPresenter.setView(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPresenter.onViewCreated();
	}

	//region Buttons click
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.settings_edit_account_row)
	public void onEditAccountClick() {
		Intent intent = new Intent(getActivity(), EditAccountActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.settings_social_networks_row)
	public void onSocialNetworksClick() {
		Intent intent = new Intent(getActivity(), SocialAccountActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.settings_play_tutorial_row)
	public void onPlayTutorialClick() {
		ToastUtils.showText(getContext(), R.string.feature_coming_soon);
	}

	@OnClick(R.id.settings_support_row)
	public void onSupportClick() {
		StringBuilder builder = new StringBuilder();

		builder.append("Client version: ").append(Build.VERSION.RELEASE).append("\n");
		builder.append("Client SDK: ").append(Build.VERSION.SDK_INT).append("\n");
		builder.append("App version: ").append(BuildConfig.VERSION_NAME).append("\n");
		builder.append("Version code: ").append(BuildConfig.VERSION_CODE).append("\n");

		if (mUserId != null) {
			builder.append("UID: ").append(mUserId).append("\n");
		}

		builder.append("\n\n");

		EmailIntentBuilder.from(getActivity())
			.to(getString(R.string.support_email))
			.subject(getString(R.string.support_subject))
			.body(builder.toString())
			.start();
	}

	@OnClick(R.id.settings_website_row)
	public void onOpenWebsiteClick() {
		startActivity(new Intent(getActivity(), WebsiteActivity.class));
	}

	@OnClick(R.id.settings_delete_account_row)
	public void onDeleteAccountClick() {
		Log.d(TAG, "delete account");
		ToastUtils.showText(getContext(), R.string.feature_coming_soon);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region SettingsView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showUserId(Integer userId) {
		mUserId = userId;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
