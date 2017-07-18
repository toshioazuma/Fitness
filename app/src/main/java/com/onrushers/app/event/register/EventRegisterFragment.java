package com.onrushers.app.event.register;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.EventComponent;
import com.onrushers.common.utils.ToastUtils;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IUser;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventRegisterFragment extends BaseFragment
	implements EventRegisterView, Validator.ValidationListener {

	public static final String TAG = "EventRegisterF";

	@Bind(R.id.event_register_toolbar)
	Toolbar mToolbar;

	@Bind(R.id.event_register_date_textview)
	TextView mDateTextView;

	@Bind(R.id.event_register_places_textview)
	TextView mPlacesTextView;

	@Bind(R.id.event_register_avatar_imageview)
	CircularImageView mAvatarImageView;

	@Bind(R.id.event_register_title_textview)
	TextView mTitleTextView;

	@Bind(R.id.event_register_user_avatar_imageview)
	CircularImageView mUserAvatarImageView;

	@Bind(R.id.event_register_user_username_textview)
	TextView mUsernameTextView;

	@Bind(R.id.event_register_user_grade_textview)
	TextView mUserGradeTextView;

	@Bind(R.id.event_register_user_firstname_input)
	@NotEmpty(messageResId = R.string.event_register_validator_firstname_required)
	@Order(1)
	EditText mFirstNameInput;

	@Bind(R.id.event_register_user_lastname_input)
	@NotEmpty(messageResId = R.string.event_register_validator_lastname_required)
	@Order(2)
	EditText mLastNameInput;

	@Bind(R.id.event_register_user_phone_input)
	@NotEmpty(messageResId = R.string.event_register_validator_phone_required)
	@Order(3)
	EditText mPhoneInput;

	@Bind(R.id.event_register_user_email_input)
	@Email(messageResId = R.string.event_register_validator_email_required)
	@Order(4)
	EditText mEmailInput;

	@Bind(R.id.event_register_price_place_textview)
	TextView mPricePlaceTextView;

	@Bind(R.id.event_register_price_total_textview)
	TextView mPriceTotalTextView;

	@Inject
	EventRegisterPresenter mPresenter;

	Validator mValidator;

	ProgressDialog mProgressDialog;

	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	public static final EventRegisterFragment newInstance(IEvent event) {
		Bundle args = new Bundle();
		args.putParcelable(Extra.EVENT, event);

		EventRegisterFragment fragment = new EventRegisterFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(EventComponent.class).inject(this);
		mPresenter.setView(this);

		mValidator = new Validator(this);
		mValidator.setValidationMode(Validator.Mode.IMMEDIATE);
		mValidator.setValidationListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_event_register, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		addNavigationBackToToolbar(mToolbar);

		if (getArguments() != null && getArguments().get(Extra.EVENT) != null) {
			IEvent event = getArguments().getParcelable(Extra.EVENT);
			mPresenter.presentEvent(event);
		}

		mPresenter.onViewCreated();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region EventRegisterView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showDate(SpannableString dateSpan) {
		mDateTextView.setText(dateSpan);
	}

	@Override
	public void showPlaces(SpannableString placesSpan) {
		mPlacesTextView.setText(placesSpan);
	}

	@Override
	public void showAvatar(String avatarUrl) {
		mAvatarImageView.setImageResource(R.drawable.ic_user_avatar_default);

		Glide.with(mAvatarImageView.getContext())
			.load(avatarUrl)
			.asBitmap()
			.centerCrop()
			.into(mAvatarImageView);
	}

	@Override
	public void showTitle(String title) {
		mTitleTextView.setText(title);
	}

	@Override
	public void showUserAvatar(String avatarUrl) {
		mUserAvatarImageView.setImageResource(R.drawable.ic_user_avatar_default);

		Glide.with(mUserAvatarImageView.getContext())
			.load(avatarUrl)
			.asBitmap()
			.centerCrop()
			.into(mUserAvatarImageView);
	}

	@Override
	public void showUserInfo(String username, String grade) {
		mUsernameTextView.setText(username);
		mUserGradeTextView.setText(grade);
	}

	@Override
	public void showUserForm(String firstName, String lastName, String email) {
		mFirstNameInput.setText(firstName);
		mLastNameInput.setText(lastName);
		mEmailInput.setText(email);
	}

	@Override
	public void showPrices(String unitPrice, String totalPrice) {
		mPricePlaceTextView.setText(unitPrice);
		mPriceTotalTextView.setText(totalPrice);
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
	public void showError(String message) {
		ToastUtils.showText(getContext(), message);
	}

	@Override
	public void showSuccess(String message) {
		ToastUtils.showText(getContext(), message);
		getActivity().finish();
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
		mPresenter.registerIndividual(mEmailInput.getText().toString());
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnClick
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.event_register_next_button)
	public void onNextClick() {
		mValidator.validate();
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
