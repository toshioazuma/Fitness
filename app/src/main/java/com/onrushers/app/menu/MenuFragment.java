package com.onrushers.app.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.onrushers.app.R;
import com.onrushers.app.common.bus.events.UserUpdateEvent;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.domain.bus.BusProvider;
import com.onrushers.domain.bus.events.InvalidTokenEvent;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends BaseFragment implements MenuView {

	@Bind(R.id.menu_user_profile_picture)
	CircularImageView mUserImageView;

	@Bind(R.id.menu_user_name_textview)
	TextView mUserNameTextView;

	@Bind(R.id.menu_user_grade_textview)
	TextView mUserGradeTextView;

	@Bind({
		R.id.menu_notifications_button,
		R.id.menu_feeds_button,
		R.id.menu_explore_button,
		R.id.menu_events_button,
		R.id.menu_settings_button
	})
	List<Button> mItemButtons;

	@Inject
	MenuPresenter mPresenter;

	Listener mListener;


	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		if (context instanceof Listener) {
			mListener = (Listener) context;
		} else {
			throw new IllegalArgumentException(getActivity() +
				" must implement MenuFragment.Listener interface");
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(MainComponent.class).inject(this);
		mPresenter.setView(this);
		BusProvider.getInstance().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPresenter.onViewCreated();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BusProvider.getInstance().unregister(this);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region MenuView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showUserName(String username) {
		mUserNameTextView.setText(username);
	}

	@Override
	public void showUserGrade(String grade) {
		mUserGradeTextView.setText(grade);
	}

	@Override
	public void showUserAvatar(String avatarUrl) {

		Glide.with(mUserImageView.getContext())
			.load(avatarUrl)
			.centerCrop()
			.into(mUserImageView);
	}

	@Override
	public void reloadView() {
		mPresenter.representUser();
	}

	@Override
	public void showLaunchPage() {
		if (mListener != null) {
			mListener.showLaunchPage();
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscribe
	//----------------------------------------------------------------------------------------------

	@Subscribe
	public void onUserUpdate(UserUpdateEvent event) {
		mPresenter.presentUser(event.getUser());
	}

	@Subscribe
	public void onInvalidateToken(InvalidTokenEvent event) {
		mPresenter.requestInvalidateSession();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Button click events
	//----------------------------------------------------------------------------------------------

	static final ButterKnife.Action<View> DESELECT = new ButterKnife.Action<View>() {
		@Override
		public void apply(View view, int index) {
			view.setSelected(false);
		}
	};

	@OnClick(R.id.menu_user_header_layout)
	public void onUserHeaderClick() {
		if (mListener != null) {
			mListener.showMyProfile();
		}
	}

	@OnClick({
		R.id.menu_notifications_button, R.id.menu_feeds_button, R.id.menu_explore_button,
		R.id.menu_events_button, R.id.menu_settings_button
	})
	public void onMenuItemClick(Button button) {
		ButterKnife.apply(mItemButtons, DESELECT);
		button.setSelected(true);
	}

	@OnClick(R.id.menu_notifications_button)
	public void onNotificationsClick() {
		if (mListener != null) {
			mListener.showNotifications();
		}
	}

	@OnClick(R.id.menu_feeds_button)
	public void onFeedsClick() {
		if (mListener != null) {
			mListener.showHome();
		}
	}

	@OnClick(R.id.menu_explore_button)
	public void onExploreClick() {
		if (mListener != null) {
			mListener.showExplore();
		}
	}

	@OnClick(R.id.menu_events_button)
	public void onEventsClick() {
		if (mListener != null) {
			mListener.showEvents();
		}
	}

	@OnClick(R.id.menu_settings_button)
	public void onSettingsClick() {
		if (mListener != null) {
			mListener.showSettings();
		}
	}

	@OnClick(R.id.menu_log_out_button)
	public void onLogOutClick() {

		new AlertDialog.Builder(getContext())
			.setTitle(R.string.log_out_confirm_title)
			.setMessage(R.string.log_out_confirm_message)
			.setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mPresenter.requestInvalidateSession();
					dialog.dismiss();
				}
			})
			.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.setCancelable(true)
			.create()
			.show();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	public interface Listener {

		void showLaunchPage();

		void showNotifications();

		void showHome();

		void showMyProfile();

		void showExplore();

		void showEvents();

		void showSettings();
	}
}
