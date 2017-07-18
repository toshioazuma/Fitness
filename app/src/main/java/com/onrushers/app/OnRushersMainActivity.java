package com.onrushers.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.common.bus.events.NetworkChangeEvent;
import com.onrushers.app.event.home.EventsFragment;
import com.onrushers.app.explore.ExploreFragment;
import com.onrushers.app.feed.create.FeedCreateActivity;
import com.onrushers.app.home.HomeFragment;
import com.onrushers.app.internal.di.HasComponent;
import com.onrushers.app.internal.di.components.DaggerMainComponent;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.app.internal.di.modules.MainModule;
import com.onrushers.app.launch.LaunchActivity;
import com.onrushers.app.menu.MenuFragment;
import com.onrushers.app.menu.MenuView;
import com.onrushers.app.notification.NotificationsFragment;
import com.onrushers.app.picture.picker.PicturePickerAgent;
import com.onrushers.app.settings.SettingsFragment;
import com.onrushers.app.user.UserProfileActivity;
import com.onrushers.domain.bus.BusProvider;
import com.onrushers.domain.bus.events.InvalidTokenEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class OnRushersMainActivity extends BaseActivity
	implements HasComponent<MainComponent>, MenuFragment.Listener, DrawerLayout.DrawerListener {

	public static final String TAG = "OnRushersMain";

	public static OnRushersMainActivity sharedInstance;

	private MainComponent mComponent;
	private MenuView      mMenuView;

	@Bind(R.id.main_appbarlayout)
	AppBarLayout mAppBarLayout;

	@Bind(R.id.main_toolbar)
	Toolbar mMainToolbar;

	@Bind(R.id.drawer_layout)
	DrawerLayout mDrawer;

	@Bind(R.id.no_connectivity_textview)
	TextView mNoConnectivityTextView;

	@Bind(R.id.content_main_framelayout)
	FrameLayout mFrameLayout;


	//region Activity life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setSupportActionBar(mMainToolbar);

		ButterKnife.bind(this);
		BusProvider.getInstance().register(this);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
			this, mDrawer, mMainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		mDrawer.setDrawerListener(toggle);
		mDrawer.addDrawerListener(this);
		toggle.syncState();

		sharedInstance = this;
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		ReactiveNetwork.observeInternetConnectivity()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Action1<Boolean>() {
				@Override
				public void call(Boolean isConnectedToInternet) {
					BusProvider.getInstance().post(new NetworkChangeEvent(isConnectedToInternet));
				}
			});
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		replaceFragment(R.id.content_main_framelayout, fragment, tag);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);

		if (fragment instanceof MenuView) {
			mMenuView = (MenuView) fragment;
		}
	}

	@Override
	public void onBackPressed() {
		if (mDrawer.isDrawerOpen(GravityCompat.START)) {
			mDrawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.action_home_post_feed) {
			startActivity(new Intent(this, FeedCreateActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (PicturePickerAgent.getCurrentAgent() == null) {
			return;
		}

		PicturePickerAgent.getCurrentAgent().handleRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	protected void onDestroy() {
		BusProvider.getInstance().unregister(this);
		super.onDestroy();
	}

	@Override
	public AppBarLayout getAppBarLayout() {
		return mAppBarLayout;
	}

	@Override
	public Toolbar getToolbar() {
		return mMainToolbar;
	}

	@Override public void clearBackStack() {

		List<String> fragmentTags = Arrays.asList(NotificationsFragment.TAG, HomeFragment.TAG,
			ExploreFragment.TAG, EventsFragment.TAG, SettingsFragment.TAG);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		List<String> removedTags = new ArrayList<>();

		for (String fragmentTag : fragmentTags) {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
			if (fragment != null) {
				transaction.remove(fragment);
				removedTags.add(fragmentTag);
			}
		}

		transaction.commitNow();

		Log.d(TAG, "Removing " + removedTags.size() + " fragments; " + removedTags);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region HasComponent<MainComponent>
	//----------------------------------------------------------------------------------------------

	@Override
	public MainComponent getComponent() {

		if (mComponent == null) {
			mComponent = DaggerMainComponent.builder()
				.applicationComponent(getApplicationComponent())
				.activityModule(getActivityModule())
				.dataModule(getDataModule())
				.mainModule(new MainModule())
				.build();
		}
		return mComponent;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region DrawerLayout.DrawerListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onDrawerSlide(View drawerView, float slideOffset) {

	}

	@Override
	public void onDrawerOpened(View drawerView) {
		if (mMenuView != null) {
			mMenuView.reloadView();
		}
		mFrameLayout.setClickable(false);
	}

	@Override
	public void onDrawerClosed(View drawerView) {
		mFrameLayout.setClickable(true);
	}

	@Override
	public void onDrawerStateChanged(int newState) {

	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region MenuFragment.Listener
	//----------------------------------------------------------------------------------------------

	@Override
	public void showLaunchPage() {
		startActivity(new Intent(this, LaunchActivity.class));
		finish();
	}

	@Override
	public void showNotifications() {
		clearBackStack();
		replaceCurrentFragment(new NotificationsFragment(), NotificationsFragment.TAG);
		mDrawer.closeDrawer(GravityCompat.START);
	}

	@Override
	public void showHome() {
		clearBackStack();
		replaceCurrentFragment(new HomeFragment(), HomeFragment.TAG);
		mDrawer.closeDrawer(GravityCompat.START);
	}

	@Override
	public void showMyProfile() {
		mDrawer.closeDrawer(GravityCompat.START);
		Intent intent = new Intent(this, UserProfileActivity.class);
		startActivity(intent);
	}

	@Override
	public void showExplore() {
		clearBackStack();
		mDrawer.closeDrawer(GravityCompat.START);
		replaceCurrentFragment(new ExploreFragment(), ExploreFragment.TAG);
	}

	@Override
	public void showEvents() {
		clearBackStack();
		replaceCurrentFragment(new EventsFragment(), EventsFragment.TAG);
		mDrawer.closeDrawer(GravityCompat.START);
	}

	@Override
	public void showSettings() {
		clearBackStack();
		replaceCurrentFragment(new SettingsFragment(), SettingsFragment.TAG);
		mDrawer.closeDrawer(GravityCompat.START);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscriber
	//----------------------------------------------------------------------------------------------

	@Subscribe
	public void onConnectivityChange(NetworkChangeEvent event) {
		mNoConnectivityTextView.setVisibility(event.isAvailable() ? View.GONE : View.VISIBLE);
	}

	@Subscribe
	public void onInvalidateToken(InvalidTokenEvent event) {

		if (isFinishing()) {
			return;
		}

		finish();
		startActivity(new Intent(this, LaunchActivity.class));
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
