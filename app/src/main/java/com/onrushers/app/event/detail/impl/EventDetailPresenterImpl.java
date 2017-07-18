package com.onrushers.app.event.detail.impl;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.onrushers.app.R;
import com.onrushers.app.common.bus.events.EventRegisterEvent;
import com.onrushers.app.event.detail.EventDetailPresenter;
import com.onrushers.app.event.detail.EventDetailView;
import com.onrushers.app.file.FileClient;
import com.onrushers.domain.bus.BusProvider;
import com.onrushers.domain.business.interactor.event.GetEventInteractor;
import com.onrushers.domain.business.lazy.LazyEvent;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IEventRegisterResult;
import com.onrushers.domain.business.model.ILocation;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.EventStateType;
import com.onrushers.domain.common.DefaultSubscriber;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.List;

import javax.inject.Inject;

public class EventDetailPresenterImpl implements EventDetailPresenter {

	private EventDetailView mView;
	private IEvent          mEvent;

	private final Context          mContext;
	private final FileClient       mFileClient;
	private final SimpleDateFormat mDateFormat;

	private final GetEventInteractor mGetEventInteractor;

	@Inject
	public EventDetailPresenterImpl(Context context, FileClient fileClient, SimpleDateFormat dateFormat,
	                                GetEventInteractor getEventInteractor) {
		mContext = context;
		mFileClient = fileClient;
		mDateFormat = dateFormat;

		mGetEventInteractor = getEventInteractor;
	}

	@Override
	public void setView(EventDetailView view) {
		mView = view;
		BusProvider.getInstance().register(this);
	}

	@Override
	public void presentEvent(IEvent event) {
		mEvent = event;

		if (event == null || mView == null) {
			return;
		} else if (event.isLazy()) {
			mView.showLoading();

			/** Get event individually and redo the present event call */
			mGetEventInteractor.setEvent(event);
			mGetEventInteractor.execute(new DefaultSubscriber<IEvent>() {

				@Override
				public void onError(Throwable e) {
					mView.hideLoading();
					mView.showError(mContext.getString(R.string.event_detail_messages_failed_to_load));
					mView.dismissView();
				}

				@Override
				public void onNext(IEvent event) {
					presentEvent(event);
					mView.hideLoading();
				}
			});
			return;
		}

		List<IUser> heroes = (List<IUser>) event.getHeroes();
		if (heroes != null && !heroes.isEmpty()) {

			int herosCount = heroes.size();
			int registerationCount = event.getRegisteredUsers().size();
			int deltaCount = registerationCount - herosCount;

			String usersCountLeftText = mContext
				.getString(R.string.event_detail_plus_count_onrushers, deltaCount);

			mView.showHerosRelation(heroes, getYourHerosCountSpan(herosCount), usersCountLeftText);
		}


		mView.showTitle(event.getTitle());
		mView.showDescription(event.getDescription());

		if (event.getPhotosIds() != null && !event.getPhotosIds().isEmpty()) {
			mView.showPictures(event.getPhotosIds());
		}

		if (!TextUtils.isEmpty(event.getOrgnaizerPhotoId())) {
			Integer photoId = Integer.valueOf(event.getOrgnaizerPhotoId());

			mFileClient.getFile(photoId, new FileClient.Receiver() {
				@Override
				public void onGetFileUrl(String fileUrl) {
					mView.showAvatar(fileUrl);
				}
			});
		}

		if (event.getPrice() == 0.0) {
			mView.showPrice(new SpannableString(mContext.getString(R.string.event_terms_free)));
		} else {
			String currencySymbol = Currency.getInstance(event.getCurrency()).getSymbol();
			mView.showPrice(new SpannableString(mContext.getString(
				R.string.event_terms_price_per_person, event.getPrice().intValue(), currencySymbol)));
		}

		List<? extends IEventRegisterResult> registeredUsers = event.getRegisteredUsers();
		if (registeredUsers != null && !registeredUsers.isEmpty()) {

			if (mEvent.isMine()) {
				mView.showState(EventStateType.Registered);
			} else if (event.getPlacesLeft() == 0) {
				mView.showState(EventStateType.Full);
			} else {
				mView.showState(EventStateType.Registration);
			}
		} else {
			mView.showState(EventStateType.Registration);
		}

		mDateFormat.applyPattern("dd MMMM yyyy - HH'H'");
		String dateText = mDateFormat.format(event.getDate());
		String fullDateText = mContext.getString(R.string.event_detail_date, dateText);

		int start = fullDateText.indexOf(dateText);
		int end = start + dateText.length();

		SpannableString dateSpan = new SpannableString(fullDateText);
		dateSpan.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mView.showDate(dateSpan);

		String placesLeftFullText = mContext.getString(
			R.string.event_detail_places_left, event.getPlacesLeft(), event.getPlacesMax());

		start = placesLeftFullText.indexOf(String.valueOf(event.getPlacesLeft()));

		String placesMaxText = String.valueOf(event.getPlacesMax());
		end = placesLeftFullText.lastIndexOf(placesMaxText) + placesMaxText.length();

		SpannableString placesLeftSpan = new SpannableString(placesLeftFullText);
		placesLeftSpan.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mView.showPlacesLeft(placesLeftSpan);


		final String recommendedLevel;
		if (event.getRecommendedLevel() != null) {
			recommendedLevel = event.getRecommendedLevel();
		} else {
			recommendedLevel = mContext.getString(R.string.event_terms_no_recommended_level);
		}
		String levelFullText = mContext.getString(
			R.string.event_detail_recommended_level, recommendedLevel);

		start = levelFullText.indexOf(recommendedLevel);
		end = start + recommendedLevel.length();

		SpannableString levelSpan = new SpannableString(levelFullText);
		levelSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.blueSecondary)),
			start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mView.showLevel(levelSpan);

		mView.showPublic(event.getPublic());

		if (event.getLocation() != null) {
			ILocation location = event.getLocation();
			String secondaryAddress = TextUtils.join(", ", new String[]{
				TextUtils.join(" ", new String[]{location.getZipcode(), location.getCity()}),
				location.getCountry()
			});

			mView.showLocation(location.getAddress(), secondaryAddress,
				location.getLatitude(), location.getLongitude());
		}
	}

	@Override
	public void reloadView() {
		if (mEvent == null) {
			return;
		}

		presentEvent(new LazyEvent(mEvent.getId(), mEvent.getTitle()));
	}

	@Override
	public IEvent getPresentedEvent() {
		return mEvent;
	}

	@Override
	public void onDestroyView() {
		BusProvider.getInstance().unregister(this);
	}

	private SpannableString getYourHerosCountSpan(int yourHerosCount) {
		String yourHerosCountText = mContext
			.getString(R.string.event_detail_your_heros_count_registered, yourHerosCount);

		int start = yourHerosCountText.indexOf(String.valueOf(yourHerosCount));
		int end = start + String.valueOf(yourHerosCount).length();

		SpannableString yourHerosCountSpan = new SpannableString(yourHerosCountText);
		yourHerosCountSpan.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		yourHerosCountSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.blue)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		return yourHerosCountSpan;
	}

	//region Subscriber
	//----------------------------------------------------------------------------------------------

	@Subscribe
	public void onEventRegister(EventRegisterEvent event) {
		if (mEvent.getId().equals(event.getEvent().getId())) {
			presentEvent(event.getEvent());
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
