package com.onrushers.app.event.ticket.impl;

import android.content.Context;
import android.text.TextUtils;

import com.onrushers.app.R;
import com.onrushers.app.event.ticket.EventTicketPresenter;
import com.onrushers.app.event.ticket.EventTicketView;
import com.onrushers.app.file.FileClient;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.ILocation;

import java.text.SimpleDateFormat;
import java.util.Currency;

import javax.inject.Inject;

public class EventTicketPresenterImpl implements EventTicketPresenter {

	private EventTicketView mView;

	@Inject
	FileClient mFileClient;

	@Inject
	SimpleDateFormat mDateFormat;

	@Inject
	Context mContext;

	@Inject
	public EventTicketPresenterImpl() {

	}

	@Override
	public void setView(EventTicketView view) {
		mView = view;
	}

	@Override
	public void presentEvent(IEvent event) {
		if (mView == null) {
			return;
		}

		mView.showTitle(event.getTitle());
		mView.showSubtitle(event.getPublic().toString());

		if (!TextUtils.isEmpty(event.getOrgnaizerPhotoId())) {
			Integer photoId = Integer.valueOf(event.getOrgnaizerPhotoId());
			mFileClient.getFile(photoId, new FileClient.Receiver() {
				@Override
				public void onGetFileUrl(String fileUrl) {
					mView.showAvatar(fileUrl);
				}
			});
		}

		String places = mContext.getResources()
			.getQuantityString(R.plurals.event_register_places_count, 1, 1);
		mView.showPlaces(places);

		if (event.getPrice() != null && event.getPrice() > 0) {
			String currencySymbol = Currency.getInstance(event.getCurrency()).getSymbol();
			int placesCount = 1;

			String placesCountText = mContext.getResources()
				.getQuantityString(R.plurals.event_register_places_count, placesCount, placesCount);

			String unitPrice = mContext.getString(R.string.event_register_price_place_format,
				event.getPrice().intValue(), currencySymbol, placesCountText);

			mView.showPrice(unitPrice);
		} else {
			mView.showPrice(mContext.getString(R.string.event_terms_free));
		}

		mDateFormat.applyPattern(mContext.getString(R.string.event_register_date_format));
		String dateText = mDateFormat.format(event.getDate());

		mDateFormat.applyPattern(mContext.getString(R.string.event_ticket_hour_format));
		String hourText = mDateFormat.format(event.getDate());

		mView.showDate(dateText, hourText);

		if (event.getLocation() != null) {
			ILocation location = event.getLocation();
			String secondaryAddress = TextUtils.join(", ", new String[]{
				TextUtils.join(" ", new String[]{location.getZipcode(), location.getCity()}),
				location.getCountry()
			});

			mView.showLocation(location.getAddress() + "\n" + secondaryAddress);
		}

		if (event.getQRCodeParticipation() != null) {
			mView.showQrCode(event.getQRCodeParticipation());
		}
	}
}
