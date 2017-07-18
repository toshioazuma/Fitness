package com.onrushers.app.settings.website;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.activities.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebsiteActivity extends BaseActivity {

	@Bind(R.id.settings_website_webview)
	WebView mWebView;

	//region Activity life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_website);
		ButterKnife.bind(this);
		Toolbar toolbar = (Toolbar) findViewById(R.id.website_toolbar);
		setSupportActionBar(toolbar);
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient());

		if (getIntent() != null && getIntent().hasExtra(Extra.WEB_URL)) {
			mWebView.loadUrl(getIntent().getStringExtra(Extra.WEB_URL));
		} else {
			mWebView.loadUrl(getString(R.string.app_website_url));
		}
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		/** empty implementation */
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Buttons click
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.website_back_button)
	public void onBackClick() {
		onBackPressed();
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
