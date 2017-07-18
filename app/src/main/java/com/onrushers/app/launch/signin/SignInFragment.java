package com.onrushers.app.launch.signin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.LaunchComponent;
import com.onrushers.app.picture.cropper.PictureCropperMode;
import com.onrushers.app.picture.picker.PicturePickerAgent;
import com.onrushers.app.settings.website.WebsiteActivity;
import com.onrushers.common.defaults.DefaultTextWatcher;
import com.onrushers.common.fragments.DatePickerFragment;
import com.onrushers.common.utils.KeyboardUtils;
import com.onrushers.common.utils.ToastUtils;
import com.onrushers.common.widgets.ORTextInputEditText;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends BaseFragment implements SignInView,
	DatePickerFragment.OnDateObjectSetListener, Validator.ValidationListener,
	PicturePickerAgent.Callbacks {

	public static final String TAG = "SignInFragment";

	private static final String ARG_FACEBOOK_ID = "arg.facebook_id";
	private static final String ARG_LAST_NAME   = "arg.last_name";
	private static final String ARG_FIRST_NAME  = "arg.first_name";
	private static final String ARG_EMAIL       = "arg.email";
	private static final String ARG_BIRTHDATE   = "arg.birthdate";
	private static final String ARG_GENDER      = "arg.gender";

	private Callbacks mListener;

	@Bind(R.id.sign_in_scrollview)
	protected ScrollView mScrollView;

	@Bind(R.id.sign_in_content_layout)
	protected LinearLayout mContentLayout;

	@Bind(R.id.sign_in_take_picture_button)
	protected ImageButton mTakePictureButton;

	@Bind(R.id.sign_in_user_picture_imageview)
	protected CircularImageView mAvatarImageView;

	@Bind(R.id.sign_in_delete_picture_button)
	protected ImageButton mDeletePictureButton;

	@Bind(R.id.sign_in_username_layout)
	protected TextInputLayout mUsernameInputLayout;

	@Bind(R.id.sign_in_username_input)
	@NotEmpty(messageResId = R.string.sign_in_validator_required_field)
	@Order(1)
	protected ORTextInputEditText mUsernameInput;

	@Bind(R.id.sign_in_lastname_layout)
	protected TextInputLayout mLastNameInputLayout;

	@Bind(R.id.sign_in_lastname_input)
	@NotEmpty(messageResId = R.string.sign_in_validator_required_field)
	@Order(2)
	protected ORTextInputEditText mLastNameInput;

	@Bind(R.id.sign_in_firstname_layout)
	protected TextInputLayout mFirstNameInputLayout;

	@Bind(R.id.sign_in_firsttname_input)
	@NotEmpty(messageResId = R.string.sign_in_validator_required_field)
	@Order(3)
	protected ORTextInputEditText mFirstNameInput;

	@Bind(R.id.sign_in_password_layout)
	protected TextInputLayout mPasswordInputLayout;

	@Bind(R.id.sign_in_password_input)
	@Password(messageResId = R.string.sign_in_validator_password_min_length)
	@NotEmpty(messageResId = R.string.sign_in_validator_required_field)
	@Order(5)
	protected ORTextInputEditText mPasswordInput;

	@Bind(R.id.sign_in_confirm_password_layout)
	protected TextInputLayout mConfirmPasswordInputLayout;

	@Bind(R.id.sign_in_confirm_password_input)
	@ConfirmPassword(messageResId = R.string.sign_in_validator_doesnt_match_passwords)
	@Order(6)
	protected ORTextInputEditText mConfirmPasswordInput;

	@Bind(R.id.sign_in_email_layout)
	protected TextInputLayout mEmailInputLayout;

	@Bind(R.id.sign_in_email_input)
	@NotEmpty(messageResId = R.string.sign_in_validator_required_field)
	@Email(messageResId = R.string.sign_in_validator_invalid_email)
	@Order(4)
	protected ORTextInputEditText mEmailInput;

	@Bind(R.id.sign_in_birthdate_layout)
	protected TextInputLayout mBirthDatenputLayout;

	@Bind(R.id.sign_in_birthdate_input)
	@NotEmpty(messageResId = R.string.sign_in_validator_required_field)
	@Order(7)
	protected ORTextInputEditText mBirthDateInput;

	@Bind(R.id.sign_in_gender_toggle_button)
	protected ToggleButton mGenderToggleButton;

	@Bind(R.id.sign_in_unit_toggle_button)
	protected ToggleButton mUnitToggleButton;

	@Bind(R.id.sign_in_sponsor_input)
	protected ORTextInputEditText mSponsorInput;

	@Bind(R.id.sign_in_terms_textview)
	protected TextView mTermsTextView;

	protected Validator mValidator;

	protected boolean mValidationSucceeded = false;

	@Inject
	SignInPresenter mPresenter;

	DatePickerFragment mDatePicker;

	ProgressDialog mProgressDialog;


	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	public static SignInFragment newInstance(String facebookId) {

		Bundle args = new Bundle();
		args.putString(ARG_FACEBOOK_ID, facebookId);

		SignInFragment fragment = new SignInFragment();
		fragment.setArguments(args);

		return fragment;
	}

	public static SignInFragment newInstance(String facebookId, String lastName, String firstName,
	                                         String email, String birthDate, String gender) {

		Bundle args = new Bundle();
		args.putString(ARG_FACEBOOK_ID, facebookId);
		args.putString(ARG_LAST_NAME, lastName);
		args.putString(ARG_FIRST_NAME, firstName);
		args.putString(ARG_EMAIL, email);
		args.putString(ARG_BIRTHDATE, birthDate);
		args.putString(ARG_GENDER, gender);

		SignInFragment fragment = new SignInFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof Callbacks) {
			mListener = (Callbacks) context;
		} else {
			throw new RuntimeException(context.toString() +
				" must implement SignInFragment.Callbacks");
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(LaunchComponent.class).inject(this);
		mPresenter.setView(this);

		mValidator = new Validator(this);
		mValidator.setValidationMode(Validator.Mode.IMMEDIATE);
		mValidator.setValidationListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		handleArguments();

		mUsernameInput.setInputLayout(mUsernameInputLayout);
		mLastNameInput.setInputLayout(mLastNameInputLayout);
		mFirstNameInput.setInputLayout(mFirstNameInputLayout);
		mEmailInput.setInputLayout(mEmailInputLayout);
		mBirthDateInput.setInputLayout(mBirthDatenputLayout);

		mUsernameInput.addTextChangedListener(new DefaultTextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				mPresenter.searchUser(s.toString());
			}
		});

		/**
		 * Fix typeface of password inputs
		 */
		mPasswordInput.setTypeface(Typeface.DEFAULT);
		mPasswordInput.setTransformationMethod(new PasswordTransformationMethod());
		mPasswordInput.setInputLayout(mPasswordInputLayout);

		mConfirmPasswordInput.setTypeface(Typeface.DEFAULT);
		mConfirmPasswordInput.setTransformationMethod(new PasswordTransformationMethod());
		mConfirmPasswordInput.setInputLayout(mConfirmPasswordInputLayout);

		SpannableString termsSpan = mPresenter.getTermsSpan(getContext());
		if (termsSpan != null) {
			mTermsTextView.setText(termsSpan);
		}

		PicturePickerAgent
			.getAgent(this)
			.setCallbacksListener(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		PicturePickerAgent
			.getAgent(this)
			.handleActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPresenter.onDestroy();
	}

	private void handleArguments() {

		if (getArguments() != null) {
			Bundle args = getArguments();

			if (args.get(ARG_FACEBOOK_ID) != null) {
				mPresenter.setFacebookId(args.getString(ARG_FACEBOOK_ID));
			}
			if (args.get(ARG_LAST_NAME) != null) {
				mPresenter.setLastName(args.getString(ARG_LAST_NAME));
				mLastNameInput.setText(args.getString(ARG_LAST_NAME));
			}
			if (args.get(ARG_FIRST_NAME) != null) {
				mPresenter.setFirstName(args.getString(ARG_FIRST_NAME));
				mFirstNameInput.setText(args.getString(ARG_FIRST_NAME));
			}
			if (args.get(ARG_EMAIL) != null) {
				mPresenter.setEmail(args.getString(ARG_EMAIL));
				mEmailInput.setText(args.getString(ARG_EMAIL));
			}
			if (args.get(ARG_BIRTHDATE) != null) {
				/**
				 * MM/DD/YYYY
				 * MM/DD
				 * YYYY
				 */
				Calendar calendar = Calendar.getInstance();
				String birthdate = args.getString(ARG_BIRTHDATE);

				String[] birthdateParts = birthdate.split("/");

				switch (birthdateParts.length) {
					case 3:
						calendar.set(Integer.valueOf(birthdateParts[2]),
							Integer.valueOf(birthdateParts[1]), Integer.valueOf(birthdateParts[0]));
						mPresenter.selectBirthDate(calendar.getTime());
						break;
					case 2:
						calendar.set(calendar.get(Calendar.YEAR) - 20,
							Integer.valueOf(birthdateParts[1]), Integer.valueOf(birthdateParts[0]));
						mPresenter.selectBirthDate(calendar.getTime());
						break;
					case 1:
						calendar.set(Calendar.YEAR, Integer.valueOf(birthdateParts[0]));
						mPresenter.selectBirthDate(calendar.getTime());
						break;
					default:
						break;
				}
			}
			if (args.get(ARG_GENDER) != null) {
				if ("male".equals(args.getString(ARG_GENDER))) {
					mPresenter.selectMaleGender();
					mGenderToggleButton.setChecked(true);
				} else if ("female".equals(args.getString(ARG_GENDER))) {
					mPresenter.selectFemaleGender();
					mGenderToggleButton.setChecked(false);
				}
			}

		} else {
			/**
			 * Turn to 'male' gender by default
			 */
			mPresenter.selectMaleGender();
			mGenderToggleButton.setChecked(true);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region DatePickerFragment.OnDateObjectSetListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onDateSet(DatePicker view, Date date) {
		mPresenter.selectBirthDate(date);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region SignInView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showUsernameAvailable() {
		mUsernameInput.setError(null);
	}

	@Override
	public void showUsernameUnavailable() {
		mUsernameInput.setError(getString(R.string.sign_in_messages_username_not_available));
	}

	@Override
	public void showBirthDate(String formattedDate) {
		mBirthDateInput.setText(formattedDate);
	}

	@Override
	public void showLoading() {
		mProgressDialog = ProgressDialog.show(
			getContext(), getString(R.string.dialog_loading_title), getString(R.string.dialog_loading_message), true);
	}

	@Override
	public void hideLoading() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void showSignInErrorMessage(String errorMessage) {
		ToastUtils.showText(getContext(), errorMessage);
	}

	@Override
	public void onSuccessfulSignIn() {
		ToastUtils.showText(getContext(), R.string.sign_in_messages_sign_in_success);

		String email = mEmailInput.getText().toString();
		String password = mPasswordInput.getText().toString();

		mListener.onNewCreatedUser(email, password);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Validation listener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onValidationSucceeded() {
		resetTextInputEditTextErrors();
		mValidationSucceeded = true;
	}

	@Override
	public void onValidationFailed(final List<ValidationError> errors) {
		resetTextInputEditTextErrors();

		if (errors != null) {
			mValidationSucceeded = false;

			for (ValidationError error : errors) {
				View view = error.getView();
				String message = error.getCollatedErrorMessage(getContext());

				if (view instanceof ORTextInputEditText) {
					final ORTextInputEditText trEditText = (ORTextInputEditText) view;
					trEditText.setError(message);
				}
			}

			if (errors.size() > 0) {
				final ORTextInputEditText editText = (ORTextInputEditText) errors.get(0).getView();
				editText.requestFocus();

				final Rect scrollBounds = new Rect();
				mScrollView.getHitRect(scrollBounds);
				if (!editText.getLocalVisibleRect(scrollBounds)) {
					new Handler().post(new Runnable() {
						@Override
						public void run() {
							mScrollView.smoothScrollTo(0, editText.getBottom());
						}
					});
				}
			}
		} else {
			mValidationSucceeded = true;
		}
	}

	private void resetTextInputEditTextErrors() {
		ORTextInputEditText[] editTexts = {
			mUsernameInput, mLastNameInput, mFirstNameInput,
			mEmailInput, mPasswordInput, mSponsorInput
		};
		for (ORTextInputEditText editText : editTexts) {
			editText.setError(null);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Button actions
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.sign_in_action_back_button)
	public void onActionBackClick() {
		getActivity().onBackPressed();
	}

	@OnClick(R.id.sign_in_take_picture_button)
	public void onTakePictureClick() {
		PicturePickerAgent
			.getAgent(this)
			.setCropMode(PictureCropperMode.CIRCLE)
			.presentBottomSheetDialog();
	}

	@OnClick(R.id.sign_in_delete_picture_button)
	public void onDeletePictureClick() {
		mPresenter.removePicture();

		mAvatarImageView.setVisibility(View.GONE);
		mDeletePictureButton.setVisibility(View.GONE);
		mTakePictureButton.setVisibility(View.VISIBLE);
	}

	@OnClick(R.id.sign_in_birthdate_input)
	public void onBirthDateInputClick() {
		if (mDatePicker == null) {
			mDatePicker = new DatePickerFragment();
			mDatePicker.setOnDateObjectSetListener(this);
		}
		mDatePicker.show(getChildFragmentManager(), DatePickerFragment.TAG);
	}

	@OnClick(R.id.sign_in_gender_toggle_button)
	public void onGenderButtonClick(ToggleButton genderToggleButton) {
		if (genderToggleButton.isChecked()) {
			mPresenter.selectMaleGender();
		} else {
			mPresenter.selectFemaleGender();
		}
	}

	@OnClick(R.id.sign_in_unit_toggle_button)
	public void onUnitButtonClick(ToggleButton unitToggleButton) {
		if (unitToggleButton.isChecked()) {
			mPresenter.selectMetricUnit();
		} else {
			mPresenter.selectImperialUnit();
		}
	}

	@OnClick(R.id.sign_in_terms_textview)
	public void onTermsTextClick() {
		Intent intent = new Intent(getActivity(), WebsiteActivity.class);
		intent.putExtra(Extra.WEB_URL, getString(R.string.app_cgu));
		startActivity(intent);
	}

	@OnClick(R.id.sign_in_button)
	public void onSignInClick() {

		KeyboardUtils.hide(getActivity());

		/**
		 * Input fields validation
		 */
		mValidator.validate();
		if (!mValidationSucceeded) {
			return;
		}

		if (!mPresenter.isBirthDateValid()) {
			ToastUtils.showText(getContext(), R.string.sign_in_messages_invalid_birthdate);
			return;
		}

		mPresenter.setUsername(mUsernameInput.getText().toString());
		mPresenter.setLastName(mLastNameInput.getText().toString());
		mPresenter.setFirstName(mFirstNameInput.getText().toString());
		mPresenter.setPassword(mPasswordInput.getText().toString());
		mPresenter.setEmail(mEmailInput.getText().toString());

		mPresenter.createUser();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region PicturePickerAgent.Callbacks
	//----------------------------------------------------------------------------------------------

	@Override public void onPicturePickerCannotUseCamera() {
		ToastUtils.showText(getContext(), R.string.picker_picture_error_cannot_use_camera);
	}

	@Override public void onPicturePickerCameraDenied() {
		ToastUtils.showText(getContext(), R.string.picker_picture_error_camera_permission_denied);
	}

	@Override public void onPicturePickerCannotUseLibrary() {
		ToastUtils.showText(getContext(), R.string.picker_picture_error_cannot_use_library);
	}

	@Override public void onPicturePickerCreateFileDenied() {
		ToastUtils.showText(getContext(), R.string.picker_picture_error_write_permission_denied);
	}

	@Override public void onPicturePickerCannotCreateFile() {
		ToastUtils.showText(getContext(), R.string.picker_picture_error_file_creation_failed);
	}

	@Override public void onPicturePickerImportFailed() {
		ToastUtils.showText(getContext(), R.string.picker_picture_error_picture_import_failed);
	}

	@Override public void onPicturePickerGotUnknownError() {
		ToastUtils.showText(getContext(), R.string.picker_picture_error_unknown_error);
	}

	@Override public void onPicturePickerDidCropFile(File file, int tag) {
		mPresenter.postPicture(file);

		Glide.with(mAvatarImageView.getContext())
			.load(file)
			.centerCrop()
			.crossFade()
			.into(mAvatarImageView);

		mAvatarImageView.setVisibility(View.VISIBLE);
		mDeletePictureButton.setVisibility(View.VISIBLE);
		mTakePictureButton.setVisibility(View.GONE);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Public callbacks
	//----------------------------------------------------------------------------------------------

	public interface Callbacks {

		void onNewCreatedUser(String email, String password);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
