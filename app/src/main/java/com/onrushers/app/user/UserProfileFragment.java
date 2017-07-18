package com.onrushers.app.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.bus.events.UserUpdateEvent;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.feed.create.FeedCreateActivity;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.app.picture.cropper.PictureCropperMode;
import com.onrushers.app.picture.fullscreen.PictureFullscreenActivity;
import com.onrushers.app.picture.picker.PicturePickerAgent;
import com.onrushers.app.picture.picker.PicturePickerAgentCallbacksImpl;
import com.onrushers.app.user.edit.UserEditActivity;
import com.onrushers.app.user.list.UserListActivity;
import com.onrushers.app.user.list.UserListConfiguration;
import com.onrushers.app.user.tabs.UserTabsPagerAdapter;
import com.onrushers.domain.bus.BusProvider;
import com.squareup.otto.Subscribe;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends BaseFragment
	implements UserProfileView, Toolbar.OnMenuItemClickListener, ViewPager.OnPageChangeListener {

	public static final  String TAG             = "UserProfileF";
	private static final int    CURRENT_USER_ID = -1;

	@Bind(R.id.user_profile_collapsing_toolbar)
	CollapsingToolbarLayout mCollapsingToolbarLayout;

	@Bind(R.id.user_profile_cover_imageview)
	ImageView mCoverImageView;

	@Bind(R.id.user_profile_edit_profile_textview)
	TextView mEditProfileTextView;

	@Bind(R.id.user_profile_toolbar)
	Toolbar mToolbar;

	@Bind(R.id.user_profile_avatar_imageview)
	CircularImageView mAvatarImageView;

	@Bind(R.id.user_profile_follow_button)
	Button mFollowButton;

	@Bind(R.id.user_profile_hero_count_textview)
	TextView mHeroCountTextView;

	@Bind(R.id.user_profile_fan_count_textview)
	TextView mFanCountTextView;

	@Bind(R.id.user_profile_username_textview)
	TextView mUsernameTextView;

	@Bind(R.id.user_profile_grade_textview)
	TextView mGradeTextView;

	@Bind(R.id.user_profile_description_textview)
	TextView mDescriptionTextView;

	@Bind(R.id.user_profile_tabs_layout)
	TabLayout mTabsLayout;

	@Bind(R.id.user_profile_tabs_viewpager)
	ViewPager mTabsViewPager;

	@Bind(R.id.user_profile_add_post_fab)
	FloatingActionButton mAddPostFAB;

	UserTabsPagerAdapter mPagerAdapter;

	@Inject
	UserProfilePresenter mPresenter;

	Integer mUserId;

	PicturePickerAgentCallbacksImpl mPicturePickerAgentListener;

	ProgressDialog mProgressDialog;

	private String mCoverPictureUrl;
	private String mAvatarPictureUrl;

	//region Constructors
	//----------------------------------------------------------------------------------------------

	public static UserProfileFragment newInstance(int userId) {

		Bundle args = new Bundle();
		args.putInt(Extra.USER_ID, userId);

		final UserProfileFragment fragment = new UserProfileFragment();
		fragment.setArguments(args);

		return fragment;
	}

	public static UserProfileFragment newMyProfileFragment() {
		return newInstance(CURRENT_USER_ID);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(MainComponent.class).inject(this);
		BusProvider.getInstance().register(this);
		mPresenter.setView(this);

		mPicturePickerAgentListener = new PicturePickerAgentCallbacksImpl(getContext()) {
			@Override
			public void onPicturePickerDidCropFile(File file, int tag) {

				Intent createFeedIntent = new Intent(getActivity(), FeedCreateActivity.class);
				createFeedIntent.putExtra(FeedCreateActivity.EXTRA_PICTURE_FILE, file);
				startActivity(createFeedIntent);
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Integer userId = null;

		if (getArguments().containsKey(Extra.USER_ID)) {
			int argUserId = getArguments().getInt(Extra.USER_ID);
			if (argUserId != CURRENT_USER_ID) {
				userId = argUserId;
			}
		}

		mUserId = userId;

		ActionBar actionBar = getBaseActivity().getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(R.string.empty);
		}
		mToolbar.setTitle(R.string.empty);

		if (userId == null || userId == CURRENT_USER_ID) {
			mPresenter.setPresentingLoggedInUser(true);
			showMyProfileControls(true);
		} else {
			mPresenter.compareToAuthUser(userId);
		}

		mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getActivity().onBackPressed();
			}
		});

		mPagerAdapter = new UserTabsPagerAdapter(getChildFragmentManager(), userId);

		mTabsViewPager.setOffscreenPageLimit(mPagerAdapter.getCount() - 1);
		mTabsViewPager.setAdapter(mPagerAdapter);
		mTabsViewPager.addOnPageChangeListener(this);

		mTabsLayout.setupWithViewPager(mTabsViewPager);

		mTabsLayout.getTabAt(0).setIcon(R.drawable.sel_tab_feeds);
		mTabsLayout.getTabAt(1).setIcon(R.drawable.sel_tab_photos);
		mTabsLayout.getTabAt(2).setIcon(R.drawable.sel_tab_training);

		mPresenter.loadUserById(userId);
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mCoverPictureUrl != null) {
			Glide.with(this)
				.load(mCoverPictureUrl)
				.centerCrop()
				.crossFade()
				.placeholder(R.drawable.ic_default_placeholder)
				.into(mCoverImageView);

			mEditProfileTextView.setVisibility(View.GONE);
		} else if (mPresenter.isPresentingLoggedInUser()) {
			mCoverImageView.setImageResource(R.drawable.bg_cover_default);
			mEditProfileTextView.setVisibility(View.VISIBLE);
		}

		if (mAvatarPictureUrl != null) {
			Glide.with(this)
				.load(mAvatarPictureUrl)
				.centerCrop()
				.crossFade()
				.placeholder(R.drawable.ic_user_avatar_default)
				.into(mAvatarImageView);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (PicturePickerAgent.isPicturePickerSupported(requestCode)) {
			PicturePickerAgent.getAgent(this)
				.handleActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		Toolbar parentToolbar = getBaseActivity().getToolbar();
		if (parentToolbar != null && parentToolbar.getMenu() != null) {
			parentToolbar.getMenu().clear();
		}

		mTabsViewPager.setAdapter(null);
		mTabsLayout.setupWithViewPager(null);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BusProvider.getInstance().unregister(this);

		mPagerAdapter = null;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Toolbar.OnMenuItemClickListener
	//----------------------------------------------------------------------------------------------

	@Override
	public boolean onMenuItemClick(MenuItem item) {

		if (R.id.action_user_edit_profile == item.getItemId() && mPresenter.isPresentingLoggedInUser()) {
			Intent intent = new Intent(getActivity(), UserEditActivity.class);
			intent.putExtra(UserEditActivity.EXTRA_USER, mPresenter.getPresentedUser());
			startActivity(intent);
			return true;
		}
		return false;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region ViewPager.OnPageChangeListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		/** Empty implementation */
	}

	@Override
	public void onPageSelected(int position) {
		if (mUserId == null && (position == 0 || position == 1)) {
			mAddPostFAB.setVisibility(View.VISIBLE);
		} else {
			/** Hide the Add post FAB for the third tab */
			mAddPostFAB.setVisibility(View.GONE);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		/** Empty implementation */
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region UserProfileView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showMyProfileControls(boolean isMyProfile) {
		if (isMyProfile) {
			Toolbar parentToolbar = getBaseActivity().getToolbar();
			if (parentToolbar != null) {
				parentToolbar.inflateMenu(R.menu.menu_user_profile);
				parentToolbar.setOnMenuItemClickListener(this);
			} else {
				mToolbar.inflateMenu(R.menu.menu_user_profile);
				mToolbar.setOnMenuItemClickListener(this);
			}

			mFollowButton.setVisibility(View.GONE);

			if (mCoverPictureUrl == null) {
				mCoverImageView.setImageResource(R.drawable.bg_cover_default);
				mEditProfileTextView.setVisibility(View.VISIBLE);
			}

		} else {
			mAddPostFAB.setVisibility(View.GONE);
		}
	}

	@Override
	public void showUserName(String userName) {
		mUsernameTextView.setText(userName);

		mCollapsingToolbarLayout.setTitle(userName);
		mCollapsingToolbarLayout.setExpandedTitleColor(0x00ffffff);
		mCollapsingToolbarLayout.setCollapsedTitleTextColor(0xffffffff);
	}

	@Override
	public void showAvatarPictureUrl(String pictureUrl) {
		mAvatarPictureUrl = pictureUrl;
		mAvatarImageView.setImageResource(R.drawable.ic_user_avatar_default);

		Glide.with(this)
			.load(pictureUrl)
			.centerCrop()
			.crossFade()
			.into(mAvatarImageView);
	}

	@Override
	public void showCoverPictureUrl(String coverPictureUrl) {
		mCoverPictureUrl = coverPictureUrl;
		mCoverImageView.setImageResource(R.drawable.ic_default_placeholder);
		mEditProfileTextView.setVisibility(View.GONE);

		Glide.with(this)
			.load(coverPictureUrl)
			.centerCrop()
			.crossFade()
			.into(mCoverImageView);
	}

	@Override
	public void showHeroCount(SpannableString heroCountSpan) {
		mHeroCountTextView.setText(heroCountSpan);
	}

	@Override
	public void showFanCount(SpannableString fanCountSpan) {
		mFanCountTextView.setText(fanCountSpan);
	}

	@Override
	public void showUserGrade(String grade) {
		mGradeTextView.setText(grade);
	}

	@Override
	public void showDescription(String description) {
		mDescriptionTextView.setText(description);
		mDescriptionTextView.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideDescription() {
		mDescriptionTextView.setVisibility(View.GONE);
	}

	@Override
	public void showFollowState() {
		mFollowButton.setSelected(false);
		mFollowButton.setText(R.string.action_follow);
		mFollowButton.setVisibility(View.VISIBLE);
	}

	@Override
	public void showFollowingState() {
		mFollowButton.setSelected(true);
		mFollowButton.setText(R.string.action_following);
		mFollowButton.setVisibility(View.VISIBLE);
	}

	@Override
	public void showLoading() {
		mProgressDialog = ProgressDialog.show(getContext(),
			getString(R.string.dialog_loading_title), getString(R.string.dialog_loading_message), false);
	}

	@Override
	public void hideLoading() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscriber
	//----------------------------------------------------------------------------------------------

	@Subscribe
	public void onUserUpdate(UserUpdateEvent event) {
		if (event.getUser() != null) {
			mPresenter.loadUserById(event.getUser().getId());
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Button click events
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.user_profile_cover_imageview)
	public void onUserCoverClick() {
		if (mCoverPictureUrl == null) {
			return;
		}

		Intent intent = new Intent(getActivity(), PictureFullscreenActivity.class);
		intent.putExtra(Extra.PICTURE_URL, mCoverPictureUrl);

		if (Build.VERSION.SDK_INT >= 21) {
			ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
				getActivity(), mCoverImageView, getString(R.string.transitions_user_profile_picture_to_picture_fullscreen));

			startActivity(intent, options.toBundle());
		} else {
			startActivity(intent);
		}
	}

	@OnClick(R.id.user_profile_avatar_imageview)
	public void onUserAvatarClick() {
		if (mAvatarPictureUrl == null) {
			return;
		}

		Intent intent = new Intent(getActivity(), PictureFullscreenActivity.class);
		intent.putExtra(Extra.PICTURE_URL, mAvatarPictureUrl);

		if (Build.VERSION.SDK_INT >= 21) {
			ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
				getActivity(), mAvatarImageView, getString(R.string.transitions_user_profile_picture_to_picture_fullscreen));

			startActivity(intent, options.toBundle());
		} else {
			startActivity(intent);
		}
	}

	@OnClick(R.id.user_profile_hero_count_textview)
	public void onUserHerosCountClick() {
		if (!mPresenter.canShowHerosList()) {
			return;
		}

		Intent intent = new Intent(getActivity(), UserListActivity.class);
		intent.putExtra(Extra.USER_LIST_CONFIGURATION,
			UserListConfiguration.getHerosUserConfiguration(mPresenter.getPresentedUser()));
		startActivity(intent);
	}

	@OnClick(R.id.user_profile_fan_count_textview)
	public void onUserFansCountClick() {
		if (!mPresenter.canShowFansList()) {
			return;
		}

		Intent intent = new Intent(getActivity(), UserListActivity.class);
		intent.putExtra(Extra.USER_LIST_CONFIGURATION,
			UserListConfiguration.getFansUserConfiguration(mPresenter.getPresentedUser()));
		startActivity(intent);
	}

	@OnClick(R.id.user_profile_add_post_fab)
	public void onAddPostClick() {

		PicturePickerAgent.getAgent(this)
			.setCallbacksListener(mPicturePickerAgentListener)
			.setCropMode(PictureCropperMode.SQUARE)
			.presentBottomSheetDialog();
	}

	@OnClick(R.id.user_profile_follow_button)
	public void onFollowUserClick() {
		mPresenter.toggleFollowUser();
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
