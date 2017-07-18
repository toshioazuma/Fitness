package com.onrushers.data.repository.place;

import com.onrushers.data.api.service.GoogleService;
import com.onrushers.domain.boundaries.PlaceRepository;
import com.onrushers.domain.business.model.ISearchPlaceResult;

import javax.inject.Inject;

import rx.Observable;

public class PlaceApiRepositoryImpl implements PlaceRepository {

	private final GoogleService mGoogleService;

	@Inject
	public PlaceApiRepositoryImpl(GoogleService googleService) {
		mGoogleService = googleService;
	}

	@Override
	public Observable<ISearchPlaceResult> searchPlaces(String search, String location, int radius, String type, String apiKey) {
		return mGoogleService.searchPlaces(location, radius, type, search, apiKey);
	}
}
