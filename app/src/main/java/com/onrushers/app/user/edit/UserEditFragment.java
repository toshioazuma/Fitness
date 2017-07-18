package com.onrushers.app.user.edit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.UserComponent;
import com.onrushers.app.picture.cropper.PictureCropperMode;
import com.onrushers.app.picture.picker.PicturePickerAgent;
import com.onrushers.app.picture.picker.PicturePickerAgentCallbacksImpl;
import com.onrushers.common.utils.KeyboardUtils;
import com.onrushers.common.utils.ToastUtils;
import com.onrushers.domain.business.model.IUser;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserEditFragment extends BaseFragment implements UserEditView {

	public static final String TAG = "UserEditF";

	private static final int PICKER_TAG_COVER  = 10;
	private static final int PICKER_TAG_AVATAR = 11;

	@Bind(R.id.user_edit_validate_button)
	Button mValidateButton;

	@Bind(R.id.user_edit_cover_imageview)
	ImageView mCoverImageView;

	@Bind(R.id.user_edit_avatar_imageview)
	ImageView mAvatarImageView;

	@Bind(R.id.user_edit_username_input)
	EditText mUsernameInput;

	@Bind(R.id.user_edit_description_input)
	EditText mDescriptionInput;

	@Inject
	UserEditPresenter mPresenter;

	private PicturePickerAgentCallbacksImpl mPicturePickerCallbacks;
	private ProgressDialog                  mProgressDialog;


	//region Constructor
	//----------------------------------------------------------------------------------------------

	public static UserEditFragment newInstance(IUser user) {

		Bundle args = new Bundle();
		args.putParcelable(Extra.USER, user);

		UserEditFragment fragment = new UserEditFragment();
		fragment.setArguments(args);

		return fragment;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(UserComponent.class).inject(this);
		mPresenter.setView(this);

		mPicturePickerCallbacks = new PicturePickerAgentCallbacksImpl(getContext()) {
			@Override
			public void onPicturePickerDidCropFile(File file, int tag) {

				if (PICKER_TAG_AVATAR == tag) {
					Glide.with(mAvatarImageView.getContext())
						.load(file)
						.centerCrop()
						.crossFade()
						.placeholder(R.drawable.ic_user_avatar_default)
						.into(mAvatarImageView);

					mPresenter.postAvatar(file);
				} else if (PICKER_TAG_COVER == tag) {
					Glide.with(mCoverImageView.getContext())
						.load(file)
						.centerCrop()
						.crossFade()
						.placeholder(R.drawable.ic_default_placeholder)
						.into(mCoverImageView);

					mPresenter.postCover(file);
				}
			}
		};

		PicturePickerAgent.getAgent(this)
			.setCallbacksListener(mPicturePickerCallbacks);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_edit, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (getArguments() != null && getArguments().get(Extra.USER) != null) {
			mPresenter.setUser((IUser) getArguments().get(Extra.USER));
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

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Button click events
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.user_edit_validate_button)
	public void onValidateClick() {

		final String email = mUsernameInput.getText().toString();
		final String description = mDescriptionInput.getText().toString();

		if (email.length() == 0 && description.length() == 0) {
			return;
		}

		KeyboardUtils.hide(getActivity());
		mPresenter.updateInfo(email, description);
	}

	@OnClick(R.id.user_edit_cover_edit_layout)
	public void onEditCoverClick() {
		KeyboardUtils.hide(getBaseActivity());

		PicturePickerAgent.getAgent(this)
			.setTag(PICKER_TAG_COVER)
			.setCropMode(PictureCropperMode.RECTANGLE)
			.presentBottomSheetDialog();
	}

	@OnClick(R.id.user_edit_avatar_button)
	public void onEditAvatarClick() {
		KeyboardUtils.hide(getBaseActivity());

		PicturePickerAgent.getAgent(this)
			.setTag(PICKER_TAG_AVATAR)
			.setCropMode(PictureCropperMode.CIRCLE)
			.presentBottomSheetDialog();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region UserEditView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showCover(String coverUrl) {
		if (TextUtils.isEmpty(coverUrl)) {
			return;
		}
		Glide.with(mCoverImageView.getContext())
			.load(coverUrl)
			.centerCrop()
			.crossFade()
			.placeholder(R.drawable.ic_default_placeholder)
			.into(mCoverImageView);
	}

	@Override
	public void showAvatar(String avatarUrl) {
		if (TextUtils.isEmpty(avatarUrl)) {
			return;
		}
		Glide.with(mAvatarImageView.getContext())
			.load(avatarUrl)
			.centerCrop()
			.crossFade()
			.placeholder(R.drawable.ic_user_avatar_default)
			.into(mAvatarImageView);
	}

	@Override
	public void showUsername(String username) {
		mUsernameInput.setText(username);
	}

	@Override
	public void showDescription(String description) {
		mDescriptionInput.setText(description);
	}

	@Override
	public void showLoading() {
		mValidateButton.setEnabled(false);

		mProgressDialog = ProgressDialog.show(getContext(),
			getString(R.string.dialog_loading_title), getString(R.string.dialog_loading_message), false);
	}

	@Override
	public void hideLoading() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}

		mValidateButton.setEnabled(true);
	}

	@Override
	public void showSuccessMessage(int successResId) {
		ToastUtils.showText(getContext(), successResId);
		getActivity().finish();
	}

	@Override
	public void showFailureMessage(String failureMessage) {
		ToastUtils.showText(getContext(), failureMessage);
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
