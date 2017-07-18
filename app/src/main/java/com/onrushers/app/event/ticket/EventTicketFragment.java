package com.onrushers.app.event.ticket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.EventComponent;
import com.onrushers.domain.business.model.IEvent;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventTicketFragment extends BaseFragment implements EventTicketView {

	public static final String TAG = "EventTicketF";

	@Bind(R.id.event_ticket_toolbar)
	Toolbar mToolbar;

	@Bind(R.id.event_ticket_avatar_imageview)
	CircularImageView mAvatarImageView;

	@Bind(R.id.event_ticket_title_textview)
	TextView mTitleTextView;

	@Bind(R.id.event_ticket_subtitle_textview)
	TextView mSubtitleTextView;

	@Bind(R.id.event_ticket_place_textview)
	TextView mPlaceTextView;

	@Bind(R.id.event_ticket_price_textview)
	TextView mPriceTextView;

	@Bind(R.id.event_ticket_date_textview)
	TextView mDateTextView;

	@Bind(R.id.event_ticket_time_textview)
	TextView mTimeTextView;

	@Bind(R.id.event_ticket_location_textview)
	TextView mLocationTextView;

	@Bind(R.id.event_ticket_qrcode_imageview)
	ImageView mQrCodeImageView;

	@Inject
	EventTicketPresenter mPresenter;

	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	public static final EventTicketFragment newInstance(IEvent event) {
		Bundle args = new Bundle();
		args.putParcelable(Extra.EVENT, event);

		EventTicketFragment fragment = new EventTicketFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(EventComponent.class).inject(this);
		mPresenter.setView(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_event_ticket, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onNavigationBack();
			}
		});

		if (getArguments() != null && getArguments().get(Extra.EVENT) != null) {
			IEvent event = getArguments().getParcelable(Extra.EVENT);
			mPresenter.presentEvent(event);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region EventTicketView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showTitle(String title) {
		mTitleTextView.setText(title);
	}

	@Override
	public void showSubtitle(String subtitle) {
		mSubtitleTextView.setText(subtitle);
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
	public void showPlaces(String places) {
		mPlaceTextView.setText(places);
	}

	@Override
	public void showPrice(String price) {
		mPriceTextView.setText(price);
	}

	@Override
	public void showDate(String date, String time) {
		mDateTextView.setText(date);
		mTimeTextView.setText(time);
	}

	@Override
	public void showLocation(String location) {
		mLocationTextView.setText(location);
	}

	@Override
	public void showQrCode(String qrCodeUrl) {
		mQrCodeImageView.setImageResource(0);

		Glide.with(mQrCodeImageView.getContext())
			.load(qrCodeUrl)
			.asBitmap()
			.fitCenter()
			.into(mQrCodeImageView);
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
