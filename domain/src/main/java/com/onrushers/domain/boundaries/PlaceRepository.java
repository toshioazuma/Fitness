package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.ISearchPlaceResult;

import rx.Observable;

public interface PlaceRepository {

	Observable<ISearchPlaceResult> searchPlaces(String search, String location, int radius,
	                                            String type, String apiKey);

}
