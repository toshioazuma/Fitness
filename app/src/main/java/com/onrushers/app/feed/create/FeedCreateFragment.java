package com.onrushers.app.feed.create;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.onrushers.app.R;
import com.onrushers.app.common.bus.events.ReloadFeedsEvent;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.feed.adapters.FeedTaggedUsersAdapter;
import com.onrushers.app.internal.di.components.FeedComponent;
import com.onrushers.app.picture.picker.PicturePickerAgent;
import com.onrushers.app.user.picker.UserPickerActivity;
import com.onrushers.common.utils.ToastUtils;
import com.onrushers.domain.bus.BusProvider;
import com.onrushers.domain.business.model.IUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedCreateFragment extends BaseFragment
	implements FeedCreateView, Validator.ValidationListener {

	public static final String TAG = "FeedCreateF";

	private static final int REQUEST_CODE_PLACE_PICKER = 10;
	private static final int REQUEST_CODE_USER_PICKER  = 20;

	@Bind(R.id.post_feed_imageview)
	ImageView mImageView;

	@Bind(R.id.post_feed_place_layout)
	RelativeLayout mPlaceLayout;

	@Bind(R.id.post_feed_place_textview)
	TextView mPlaceTextView;

	@Bind(R.id.post_feed_tagged_users_layout)
	RelativeLayout mTaggedUsersLayout;

	@Bind(R.id.post_feed_tagged_users_recyclerview)
	RecyclerView mTaggedUsersRecyclerView;

	@Bind(R.id.post_feed_text_input)
	@NotEmpty(messageResId = R.string.post_feed_validator_text_required)
	@Order(1)
	EditText mTextInput;

	@Inject
	FeedCreatePresenter mPresenter;

	Validator mValidator;

	private ProgressDialog                  mProgressDialog;

	private File                   mPictureFile;
	private FeedTaggedUsersAdapter mTaggedUsersAdapter;


	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(FeedComponent.class).inject(this);
		mPresenter.setView(this);

		mValidator = new Validator(this);
		mValidator.setValidationMode(Validator.Mode.IMMEDIATE);
		mValidator.setValidationListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_feed_create, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		LinearLayoutManager layoutManager = new LinearLayoutManager(
			getContext(), LinearLayoutManager.HORIZONTAL, false);
		mTaggedUsersRecyclerView.setLayoutManager(layoutManager);

		mTaggedUsersAdapter = new FeedTaggedUsersAdapter();
		mTaggedUsersRecyclerView.setAdapter(mTaggedUsersAdapter);

		if (mPictureFile != null) {

			Glide.with(mImageView.getContext())
				.load(mPictureFile)
				.asBitmap()
				.centerCrop()
				.into(mImageView);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (PicturePickerAgent.isPicturePickerSupported(requestCode)) {

			PicturePickerAgent.getAgent(this)
				.handleActivityResult(requestCode, resultCode, data);

		} else if (REQUEST_CODE_PLACE_PICKER == requestCode && Activity.RESULT_OK == resultCode) {

			Place place = PlacePicker.getPlace(getContext(), data);
			if (place != null) {
				List<String> parts = new ArrayList<>();
				if (!TextUtils.isEmpty(place.getName())) {
					parts.add(place.getName().toString());
				}
				if (!TextUtils.isEmpty(place.getAddress())) {
					parts.add(place.getAddress().toString());
				}

				if (!parts.isEmpty()) {
					String placeText = TextUtils.join(", ", parts);

					mPlaceTextView.setText(placeText);
					mPlaceLayout.setVisibility(View.VISIBLE);

					mPresenter.setPlace(placeText);
				} else {
					mPlaceLayout.setVisibility(View.GONE);
				}
			}

		} else if (REQUEST_CODE_USER_PICKER == requestCode) {

			if (Activity.RESULT_OK == resultCode) {
				List<IUser> selectedUsers = data
					.getParcelableArrayListExtra(UserPickerActivity.EXTRA_SELECTED_USERS);

				mTaggedUsersAdapter.setUsers(selectedUsers);
				mPresenter.setTagUsers(selectedUsers);
			}

			if (mTaggedUsersAdapter.getItemCount() > 0) {
				mTaggedUsersLayout.setVisibility(View.VISIBLE);
			} else {
				mTaggedUsersLayout.setVisibility(View.GONE);
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Click events
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.post_feed_place_button)
	public void onPlaceClick() {

		PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
		try {
			startActivityForResult(builder.build(getActivity()), REQUEST_CODE_PLACE_PICKER);
		} catch (GooglePlayServicesRepairableException e) {
			e.printStackTrace();
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}
	}

	@OnClick(R.id.post_feed_user_button)
	public void onAddUserClick() {
		Intent intent = new Intent(getActivity(), UserPickerActivity.class);
		startActivityForResult(intent, REQUEST_CODE_USER_PICKER);
	}

	@OnClick(R.id.post_feed_action_button)
	public void onPublishFeedClick() {
		mValidator.validate();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region FeedCreateView
	//----------------------------------------------------------------------------------------------

	@Override
	public void setPicture(File pictureFile) {
		mPictureFile = pictureFile;
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

	@Override
	public void showSuccessMessage(String success) {
		ToastUtils.showText(getContext(), success);
		BusProvider.getInstance().post(new ReloadFeedsEvent());
		getActivity().finish();
	}

	@Override
	public void showErrorMessage(String error) {
		if (error != null) {
			ToastUtils.showText(getContext(), error);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Validator.ValidationListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onValidationFailed(List<ValidationError> errors) {
		if (!errors.isEmpty()) {
			String errorMessage = errors.get(0).getCollatedErrorMessage(getContext());
			ToastUtils.showText(getContext(), errorMessage);
		}
	}

	@Override
	public void onValidationSucceeded() {
		mPresenter.postPicture(mPictureFile);
		mPresenter.setText(mTextInput.getText().toString());
		mPresenter.postFeed();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
