package com.onrushers.app.event.register.impl;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.onrushers.app.R;
import com.onrushers.app.common.Font;
import com.onrushers.app.common.bus.events.EventRegisterEvent;
import com.onrushers.app.event.register.EventRegisterPresenter;
import com.onrushers.app.event.register.EventRegisterView;
import com.onrushers.app.file.FileClient;
import com.onrushers.domain.bus.BusProvider;
import com.onrushers.domain.business.interactor.event.RegisterEventIndividualInteractor;
import com.onrushers.domain.business.interactor.user.GetUserInteractor;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IEventRegisterResult;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.common.DefaultSubscriber;

import java.text.SimpleDateFormat;
import java.util.Currency;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;

public class EventRegisterPresenterImpl implements EventRegisterPresenter {

	private final GetUserInteractor                 mGetUserInteractor;
	private final RegisterEventIndividualInteractor mRegisterIndividualInteractor;

	private EventRegisterView mView;
	private IEvent            mEvent;

	@Inject
	Context mContext;

	@Inject
	SimpleDateFormat mDateFormat;

	@Inject
	FileClient mFileClient;

	@Inject
	public EventRegisterPresenterImpl(GetUserInteractor getUserInteractor, RegisterEventIndividualInteractor registerIndividualInteractor) {
		mGetUserInteractor = getUserInteractor;
		mRegisterIndividualInteractor = registerIndividualInteractor;
	}

	@Override
	public void setView(EventRegisterView view) {
		mView = view;
	}

	@Override
	public void onViewCreated() {
		mGetUserInteractor.execute(new DefaultSubscriber<IUser>() {
			@Override
			public void onNext(IUser user) {
				if (!TextUtils.isEmpty(user.getProfilePicture())) {
					Integer photoId = Integer.valueOf(user.getProfilePicture());
					mFileClient.getFile(photoId, new FileClient.Receiver() {
						@Override
						public void onGetFileUrl(String fileUrl) {
							mView.showUserAvatar(fileUrl);
						}
					});
				}

				mView.showUserInfo(user.getUsername(), user.getGradeString());
				mView.showUserForm(user.getFirstName(), user.getLastName(), user.getEmail());
			}
		});
	}

	@Override
	public void presentEvent(IEvent event) {
		if (event == null) {
			return;
		}

		mEvent = event;

		mView.showDate(getDateSpan(event));
		mView.showPlaces(getPlacesSpan(event));

		mView.showTitle(event.getTitle());

		if (!TextUtils.isEmpty(event.getOrgnaizerPhotoId())) {
			Integer photoId = Integer.valueOf(event.getOrgnaizerPhotoId());
			mFileClient.getFile(photoId, new FileClient.Receiver() {
				@Override
				public void onGetFileUrl(String fileUrl) {
					mView.showAvatar(fileUrl);
				}
			});
		}

		if (event.getPrice() != null && event.getPrice() > 0) {
			String currencySymbol = Currency.getInstance(event.getCurrency()).getSymbol();
			int placesCount = 1;

			String placesCountText = mContext.getResources()
				.getQuantityString(R.plurals.event_register_places_count, placesCount, placesCount);

			String unitPrice = mContext.getString(R.string.event_register_price_place_format,
				event.getPrice().intValue(), currencySymbol, placesCountText);

			String totalPrice = mContext.getString(R.string.event_register_price_total_format,
				placesCount * event.getPrice().intValue(), currencySymbol);

			mView.showPrices(unitPrice, totalPrice);
		} else {
			mView.showPrices("", mContext.getString(R.string.event_terms_free));
		}
	}

	@Override
	public void registerIndividual(String email) {
		mView.showLoading();

		mRegisterIndividualInteractor.setEmail(email);
		mRegisterIndividualInteractor.setEvent(mEvent);
		mRegisterIndividualInteractor.execute(new DefaultSubscriber<IEventRegisterResult>() {

			@Override
			public void onError(Throwable e) {
				mView.hideLoading();
				mView.showError(mContext.getString(R.string.event_register_messages_failed));
				e.printStackTrace();
			}

			@Override
			public void onNext(IEventRegisterResult result) {
				mEvent.setMine(true);
				EventRegisterEvent event = new EventRegisterEvent(mEvent);

				BusProvider.getInstance().post(event);
				mView.hideLoading();
				mView.showSuccess(mContext.getString(R.string.event_register_messages_successful));
			}
		});
	}

	private final SpannableString getDateSpan(IEvent event) {
		mDateFormat.applyPattern(mContext.getString(R.string.event_register_date_format));
		String dateText = mDateFormat.format(event.getDate());

		mDateFormat.applyPattern(mContext.getString(R.string.event_register_hour_format));
		String hourText = mDateFormat.format(event.getDate());

		SpannableString spanString = new SpannableString((dateText + hourText).toUpperCase());
		int start = spanString.toString().indexOf(hourText);
		int end = start + hourText.length();

		applySpanStyle(spanString, start, end);
		return spanString;
	}

	private final SpannableString getPlacesSpan(IEvent event) {
		String countText = mContext.getString(R.string.event_register_places_format,
			event.getPlacesLeft(), event.getPlacesMax());
		String labelText = mContext.getString(R.string.event_register_places_suffix);

		SpannableString spanString = new SpannableString((countText + labelText).toUpperCase());
		int start = spanString.toString().indexOf(countText);
		int end = start + countText.length();

		applySpanStyle(spanString, start, end);
		return spanString;
	}

	private final void applySpanStyle(SpannableString spanString, int start, int end) {
		int darkColor = ContextCompat.getColor(mContext, R.color.darkGray);

		spanString.setSpan(new ForegroundColorSpan(darkColor),
			start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanString.setSpan(new RelativeSizeSpan(1.2f),
			start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanString.setSpan(new CalligraphyTypefaceSpan(Typeface.createFromAsset(mContext.getAssets(),
			Font.MONTSERRAT_BOLD)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

}
