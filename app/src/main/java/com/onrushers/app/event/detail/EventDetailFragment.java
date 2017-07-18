package com.onrushers.app.event.detail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.onrushers.app.R;
import com.onrushers.app.common.Constant;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.event.register.EventRegisterActivity;
import com.onrushers.app.event.ticket.EventTicketActivity;
import com.onrushers.app.feed.adapters.views.FeedTagUsersView;
import com.onrushers.app.file.FileClient;
import com.onrushers.app.internal.di.components.EventComponent;
import com.onrushers.app.location.event.LocationEventMapActivity;
import com.onrushers.app.picture.gallery.PictureGalleryPageAdapter;
import com.onrushers.common.utils.ToastUtils;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.EventStateType;
import com.onrushers.domain.business.type.Gender;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends BaseFragment
	implements EventDetailView, OnMapReadyCallback, GoogleMap.OnMapClickListener {

	public static final String TAG = "EventDetailF";

	//region Views
	//----------------------------------------------------------------------------------------------

	@Bind(R.id.event_detail_gallery_pager)
	ViewPager mPicturesPager;

	@Bind(R.id.event_detail_pager_indicator)
	CircleIndicator mPagerCircleIndicator;

	@Bind(R.id.event_detail_picture_imageview)
	ImageView mPictureImageView;

	@Bind(R.id.event_detail_category_textview)
	TextView mCategoryTextView;

	@Bind(R.id.event_detail_price_textview)
	TextView mPriceTextView;

	@Bind(R.id.event_detail_toolbar)
	Toolbar mToolbar;

	@Bind(R.id.event_detail_avatar_imageview)
	CircularImageView mAvatarImageView;

	@Bind(R.id.event_detail_title_textview)
	TextView mTitleTextView;

	@Bind(R.id.event_detail_heros_layout)
	LinearLayout mHerosLayout;

	@Bind(R.id.event_detail_heros_recyclerview)
	FeedTagUsersView mHerosView;

	@Bind(R.id.event_detail_heros_count_textview)
	TextView mHerosCountTextView;

	@Bind(R.id.event_detail_people_left_count_textview)
	TextView mPeopleLeftCountTextView;

	@Bind(R.id.event_detail_action_layout)
	RelativeLayout mActionLayout;

	@Bind(R.id.event_detail_register_button)
	Button mRegisterButton;

	@Bind(R.id.event_detail_ticket_button)
	Button mTicketButton;

	@Bind(R.id.event_detail_full_textview)
	TextView mFullTextView;

	@Bind(R.id.event_detail_date_textview)
	TextView mDateTextView;

	@Bind(R.id.event_detail_places_left_textview)
	TextView mPlacesLeftTextView;

	@Bind(R.id.event_detail_grade_suggest_textview)
	TextView mGradeSuggestTextView;

	@Bind(R.id.event_detail_public_female_imageview)
	ImageView mPublicFemaleImageView;

	@Bind(R.id.event_detail_public_male_imageview)
	ImageView mPublicMaleImageView;

	@Bind(R.id.event_detail_description_textview)
	TextView mDescriptionTextView;

	@Bind(R.id.event_detail_address_primary_textview)
	TextView mAddressPrimaryTextView;

	@Bind(R.id.event_detail_address_secondary_textview)
	TextView mAddressSecondaryTextView;

	@Inject
	EventDetailPresenter mPresenter;

	ProgressDialog mProgressDialog;

	PictureGalleryPageAdapter mGalleryAdapter;

	MapFragment mLocationMapFragment;
	GoogleMap   mGoogleMap;
	LatLng      mLocationLatLng;
	Marker      mLocationMarker;

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Initializer
	//----------------------------------------------------------------------------------------------

	public static final EventDetailFragment newInstance(IEvent event) {
		Bundle args = new Bundle();
		args.putParcelable(Extra.EVENT, event);

		EventDetailFragment fragment = new EventDetailFragment();
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
		getComponent(EventComponent.class).inject(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		addNavigationBackToToolbar(mToolbar);
		mPresenter.setView(this);

		mGalleryAdapter = new PictureGalleryPageAdapter(getChildFragmentManager());
		mPicturesPager.setAdapter(mGalleryAdapter);

		mHerosView.hideTagImageView();

		mLocationMapFragment = (MapFragment) getActivity().getFragmentManager()
			.findFragmentById(R.id.event_detail_location_map_fragment);
		mLocationMapFragment.getMapAsync(this);

		if (getArguments() != null && getArguments().get(Extra.EVENT) != null) {
			IEvent event = getArguments().getParcelable(Extra.EVENT);
			mPresenter.presentEvent(event);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mPresenter.reloadView();
	}

	@Override
	public void onDestroyView() {
		mPresenter.onDestroyView();
		super.onDestroyView();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnClick
	//----------------------------------------------------------------------------------------------
	
	@OnClick(R.id.event_detail_register_button)
	public void onRegisterClick() {
		Intent intent = new Intent(getActivity(), EventRegisterActivity.class);
		intent.putExtra(Extra.EVENT, mPresenter.getPresentedEvent());
		startActivity(intent);
	}

	@OnClick(R.id.event_detail_ticket_button)
	public void onMyTicketClick() {
		Intent intent = new Intent(getActivity(), EventTicketActivity.class);
		intent.putExtra(Extra.EVENT, mPresenter.getPresentedEvent());
		startActivity(intent);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region EventDetailView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showPictures(List<Integer> picturesIds) {
		mGalleryAdapter.setPicturesIds(picturesIds);
		mPagerCircleIndicator.setViewPager(mPicturesPager);
	}

	@Override
	public void showPrice(SpannableString priceSpan) {
		mPriceTextView.setText(priceSpan);
	}

	@Override
	public void showAvatar(String avatarUrl) {
		mAvatarImageView.setImageResource(R.drawable.ic_default_placeholder);

		Glide.with(mAvatarImageView.getContext())
			.load(avatarUrl)
			.centerCrop()
			.into(mAvatarImageView);
	}

	@Override
	public void showTitle(String title) {
		mTitleTextView.setText(title);
	}

	@Override
	public void showHerosRelation(List<IUser> herosList, SpannableString herosCountSpan, String usersCountLeft) {
		if (herosList.isEmpty()) {
			mHerosLayout.setVisibility(View.GONE);
		} else {
			mHerosLayout.setVisibility(View.VISIBLE);
			mHerosView.setUsers(herosList);

			mHerosCountTextView.setText(herosCountSpan);
			mPeopleLeftCountTextView.setText(usersCountLeft);
		}
	}

	@Override
	public void showState(EventStateType stateType) {
		switch (stateType) {
			case Full:
				int redColor = ContextCompat.getColor(getContext(), R.color.red);
				mActionLayout.setBackgroundColor(redColor);
				mPlacesLeftTextView.setTextColor(redColor);
				mRegisterButton.setVisibility(View.GONE);
				mFullTextView.setVisibility(View.VISIBLE);
				break;
			case Registered:
				int blueColor = ContextCompat.getColor(getContext(), R.color.blue);
				mActionLayout.setBackgroundColor(blueColor);
				mRegisterButton.setVisibility(View.GONE);
				mTicketButton.setVisibility(View.VISIBLE);
				break;
			case Registration:
				/** leave it as default */
				break;
		}
	}

	@Override
	public void showDate(SpannableString dateSpan) {
		mDateTextView.setText(dateSpan);
	}

	@Override
	public void showPlacesLeft(SpannableString placesLeftSpan) {
		mPlacesLeftTextView.setText(placesLeftSpan);
	}

	@Override
	public void showLevel(SpannableString levelSpan) {
		mGradeSuggestTextView.setText(levelSpan);
	}

	@Override
	public void showPublic(Gender gender) {
		switch (gender) {
			case Female:
				mPublicMaleImageView.setVisibility(View.GONE);
				break;
			case Male:
				mPublicFemaleImageView.setImageResource(View.GONE);
				break;
		}
	}

	@Override
	public void showDescription(String description) {
		mDescriptionTextView.setText(description);
	}

	@Override
	public void showLocation(String addressPrimary, String addressSecondary, Double lat, Double lng) {
		mAddressPrimaryTextView.setText(addressPrimary);
		mAddressSecondaryTextView.setText(addressSecondary);

		if (lat != null && lng != null) {
			mLocationLatLng = new LatLng(lat, lng);

			if (mLocationMarker != null) {
				mLocationMarker.remove();
			}
		}
	}

	@Override
	public void showLoading() {
		mProgressDialog = ProgressDialog.show(getContext(),
			getString(R.string.dialog_loading_title), getString(R.string.dialog_loading_message), true);
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
	public void dismissView() {
		getActivity().finish();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region com.google.android.gms.maps.OnMapReadyCallback
	//----------------------------------------------------------------------------------------------

	@Override
	public void onMapReady(GoogleMap googleMap) {
		if (null == googleMap) {
			return;
		}
		mGoogleMap = googleMap;

		UiSettings settings = googleMap.getUiSettings();
		settings.setAllGesturesEnabled(false);

		googleMap.setOnMapClickListener(this);

		if (mLocationLatLng != null) {
			MarkerOptions options = new MarkerOptions()
				.position(mLocationLatLng)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_marker_black))
				.draggable(false);

			mLocationMarker = mGoogleMap.addMarker(options);

			CameraUpdate cameraUpdate = CameraUpdateFactory
				.newLatLngZoom(mLocationLatLng, Constant.MAP_USER_DEFAULT_ZOOM);
			mGoogleMap.animateCamera(cameraUpdate);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region GoogleMap.OnMapClickListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onMapClick(LatLng latLng) {
		if (mLocationLatLng == null) {
			return;
		}

		Intent intent = new Intent(getActivity(), LocationEventMapActivity.class);
		intent.putExtra(Extra.EVENT, mPresenter.getPresentedEvent());
		startActivity(intent);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
