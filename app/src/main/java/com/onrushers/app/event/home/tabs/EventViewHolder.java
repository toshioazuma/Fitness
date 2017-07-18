package com.onrushers.app.event.home.tabs;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.onrushers.app.R;
import com.onrushers.app.event.detail.OnEventDetailListener;
import com.onrushers.app.file.Downloader;
import com.onrushers.app.file.FileClient;
import com.onrushers.app.picture.gallery.PictureGalleryPageAdapter;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.ILocation;

import java.text.SimpleDateFormat;
import java.util.Currency;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class EventViewHolder extends RecyclerView.ViewHolder {

	@Bind(R.id.card_event_picture_viewpager)
	ViewPager mPictureViewPager;

	@Bind(R.id.card_event_picture_circleindicator)
	CircleIndicator mPictureCircleIndicator;

	@Bind(R.id.card_event_full_layout)
	RelativeLayout mFullLayout;

	@Bind(R.id.card_event_top_layout)
	RelativeLayout mTopLayout;

	@Bind(R.id.card_event_avatar_imageview)
	ImageView mAvatarImageView;

	@Bind(R.id.card_event_title_textview)
	TextView mTitleTextView;

	@Bind(R.id.card_event_subtitle_textview)
	TextView mSubtitleTextView;

	@Bind(R.id.card_event_bottom_layout)
	RelativeLayout mBottomLayout;

	@Bind(R.id.card_event_price_ticket_container)
	FrameLayout mPriceTicketContainer;

	@Bind(R.id.card_event_price_textview)
	TextView mPriceTextView;

	@Bind(R.id.card_event_date_textview)
	TextView mDateTextView;

	@Bind(R.id.card_event_places_textview)
	TextView mPlacesTextView;

	@Bind(R.id.card_event_location_textview)
	TextView mLocationTextView;

	@Bind(R.id.card_event_my_ticket_imageview)
	ImageView mMyTicketImageView;

	@Bind(R.id.card_event_bottom_background_view)
	View mBottomBackgroundView;

	static SimpleDateFormat sDateFormat;

	private final OnEventDetailListener mDetailListener;

	private final PictureGalleryPageAdapter mGalleryAdapter;
	private final GestureDetectorCompat     mTapGestureDetector;

	private IEvent mEvent;


	public static final EventViewHolder newInstance(ViewGroup parent, OnEventDetailListener detailListener, FragmentManager fragmentManager) {
		View itemView = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.card_event, parent, false);
		return new EventViewHolder(itemView, detailListener, fragmentManager);
	}

	private EventViewHolder(View itemView, OnEventDetailListener detailListener, FragmentManager fragmentManager) {
		super(itemView);
		ButterKnife.bind(this, itemView);

		mDetailListener = detailListener;

		mTapGestureDetector = new GestureDetectorCompat(itemView.getContext(), new EventTapGestureListener());

		mGalleryAdapter = new PictureGalleryPageAdapter(fragmentManager);

		mPictureViewPager.setAdapter(mGalleryAdapter);
		mPictureViewPager.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				mTapGestureDetector.onTouchEvent(motionEvent);
				return false;
			}
		});

		if (sDateFormat == null) {
			sDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mBottomLayout.setZ(100);
			mPriceTicketContainer.setZ(110);
			mBottomBackgroundView.setZ(10);
		}
	}

	public void setEvent(IEvent event) {
		mEvent = event;

		mTitleTextView.setText(event.getTitle());

		if (event.getLocation() != null) {
			ILocation location = event.getLocation();
			mLocationTextView.setText(TextUtils.join(", ", new String[]{
				location.getCity(),
				location.getCountry()
			}));
		} else {
			mLocationTextView.setText("");
		}

		if (event.getDate() != null) {
			mDateTextView.setText(event.getDate().toString());
		}

		if (event.getPlacesLeft() == 0) {
			mTopLayout.setBackground(null);
			mBottomLayout.setBackground(null);
			mFullLayout.setVisibility(View.VISIBLE);
			mBottomBackgroundView.setVisibility(View.GONE);
		} else {
			mTopLayout.setBackgroundResource(R.drawable.sh_vertical_black_to_clear);
			mFullLayout.setVisibility(View.GONE);
			mBottomBackgroundView.setVisibility(View.VISIBLE);
		}

		String placesText = itemView.getContext().getString(R.string.event_terms_places_left,
			event.getPlacesLeft(), event.getPlacesMax());
		mPlacesTextView.setText(placesText);

		if (event.isMine()) {
			mPriceTextView.setVisibility(View.INVISIBLE);
			mMyTicketImageView.setVisibility(View.VISIBLE);
		} else if (event.getPrice() == null || event.getPrice() == 0) {
			mPriceTextView.setText(R.string.event_terms_free);
			mPriceTextView.setVisibility(View.VISIBLE);
			mMyTicketImageView.setVisibility(View.INVISIBLE);
		} else {
			String currencySymbol = Currency.getInstance(event.getCurrency()).getSymbol();
			String priceText = itemView.getContext().getString(
				R.string.event_terms_price_per_person, event.getPrice().intValue(), currencySymbol);

			mPriceTextView.setText(priceText);
			mPriceTextView.setVisibility(View.VISIBLE);
			mMyTicketImageView.setVisibility(View.INVISIBLE);
		}

		if (event.getDate() != null) {
			mDateTextView.setText(sDateFormat.format(event.getDate()));
		}

		if (!TextUtils.isEmpty(event.getOrgnaizerPhotoId())) {
			Integer photoId = Integer.valueOf(event.getOrgnaizerPhotoId());

			/** set placeholder manually */
			mAvatarImageView.setImageResource(R.drawable.ic_user_avatar_default);

			String fileUrl = Downloader.Companion.getInstance().resourceUrl(photoId);
			Glide.with(mAvatarImageView.getContext())
				.load(fileUrl)
				.asBitmap()
				.centerCrop()
				.into(mAvatarImageView);
		} else {
			mAvatarImageView.setImageResource(R.drawable.ic_user_avatar_default);
		}

		if (event.getPhotosIds() != null && !event.getPhotosIds().isEmpty()) {
			mGalleryAdapter.setPicturesIds(event.getPhotosIds());
			mPictureCircleIndicator.setViewPager(mPictureViewPager);
		} else {
			mGalleryAdapter.setPicturesIds(null);
		}
	}

	private class EventTapGestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			/** allows single tap */
			if (mDetailListener != null) {
				mDetailListener.onEventDetail(mEvent);
			}
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			/** allows fling event */
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			/** allows scroll event */
			return true;
		}
	}
}
