package com.onrushers.app.settings.edit_account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.onrushers.app.R;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.common.utils.ToastUtils;
import com.onrushers.domain.business.type.Gender;
import com.onrushers.domain.business.type.Unit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditAccountFragment extends BaseFragment
	implements EditAccountView, View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {

	public static final String TAG = "EditAccountF";

	@Bind(R.id.edit_account_last_name_input)
	EditText mLastNameInput;

	@Bind(R.id.edit_account_first_name_input)
	EditText mFirstNameInput;

	@Bind(R.id.edit_account_location_input)
	EditText mLocationInput;

	@Bind(R.id.edit_account_email_input)
	EditText mEmailInput;

	@Bind(R.id.edit_account_phone_input)
	EditText mPhoneInput;

	@Bind(R.id.edit_account_website_input)
	EditText mWebsiteInput;

	@Bind(R.id.edit_account_gender_imageview)
	ImageView mGenderImageView;

	@Bind(R.id.edit_account_unit_toggle_button)
	ToggleButton mUnitToggleButton;

	@Inject
	EditAccountPresenter mPresenter;

	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(MainComponent.class).inject(this);
		mPresenter.setView(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit_account, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPresenter.onViewCreated();

		mLastNameInput.setOnFocusChangeListener(this);
		mFirstNameInput.setOnFocusChangeListener(this);
		mLocationInput.setOnFocusChangeListener(this);
		mEmailInput.setOnFocusChangeListener(this);
		mPhoneInput.setOnFocusChangeListener(this);
		mWebsiteInput.setOnFocusChangeListener(this);

		mUnitToggleButton.setOnCheckedChangeListener(this);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region EditAccountView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showUserLastName(String lastName) {
		mLastNameInput.setText(lastName);
	}

	@Override
	public void showUserFirstName(String firstName) {
		mFirstNameInput.setText(firstName);
	}

	@Override
	public void showUserLocation(String location) {
		mLocationInput.setText(location);
	}

	@Override
	public void showUserEmail(String email) {
		mEmailInput.setText(email);
	}

	@Override
	public void showUserPhone(String phone) {
		mPhoneInput.setText(phone);
	}

	@Override
	public void showUserWebsite(String website) {
		mWebsiteInput.setText(website);
	}

	@Override
	public void showUserGender(Gender gender) {
		if (gender == Gender.Male) {
			mGenderImageView.setImageResource(R.drawable.ic_gender_male);
		} else if (gender == Gender.Female) {
			mGenderImageView.setImageResource(R.drawable.ic_gender_female);
		} else {
			mGenderImageView.setImageResource(0);
		}
	}

	@Override
	public void showUserUnit(Unit unit) {
		if (unit == Unit.Metric) {
			mUnitToggleButton.setChecked(true);
		} else if (unit == Unit.Imperial) {
			mUnitToggleButton.setChecked(false);
		}
	}

	@Override
	public void showUpdateError(String message) {
		ToastUtils.showText(getContext(), message);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region View.OnFocusChangeListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onFocusChange(View view, boolean hasFocus) {

		if (!hasFocus && null != view) {
			if (mLastNameInput.equals(view)) {
				mPresenter.setUserLastName(mLastNameInput.getText().toString());
			} else if (mFirstNameInput.equals(view)) {
				mPresenter.setUserFirstName(mFirstNameInput.getText().toString());
			} else if (mLocationInput.equals(view)) {

			} else if (mEmailInput.equals(view)) {
				mPresenter.setUserEmail(mEmailInput.getText().toString());
			} else if (mPhoneInput.equals(view)) {

			} else if (mWebsiteInput.equals(view)) {

			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region CompoundButton.OnCheckedChangeListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
		if (compoundButton.equals(mUnitToggleButton)) {
			mPresenter.setUserUnit(isChecked ? Unit.Metric : Unit.Imperial);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
