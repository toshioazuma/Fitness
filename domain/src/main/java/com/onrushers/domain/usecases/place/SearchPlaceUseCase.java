package com.onrushers.domain.usecases.place;

import com.onrushers.domain.boundaries.PlaceRepository;
import com.onrushers.domain.business.interactor.place.SearchPlaceInteractor;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class SearchPlaceUseCase extends UseCase implements SearchPlaceInteractor {

	private final PlaceRepository mPlaceRepository;

	private String mApiKey;
	private String mSearchText;
	private String mLocationText;
	private String mType;
	private int    mRadius;

	@Inject
	public SearchPlaceUseCase(PlaceRepository placeRepository, ThreadExecutor threadExecutor,
	                          PostExecutionThread postExecutionThread) {
		super(threadExecutor, postExecutionThread);
		mPlaceRepository = placeRepository;
	}

	@Override
	public void setApiKey(String apiKey) {
		mApiKey = apiKey;
	}

	@Override
	public void setSearchText(String searchText) {
		mSearchText = searchText;
	}

	@Override
	public void setType(String type) {
		mType = type;
	}

	@Override
	public void setLocation(double latitude, double longitude) {
		mLocationText = String.format("%f,%f", latitude, longitude);
	}

	@Override
	public void setRadius(int radius) {
		mRadius = radius;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		return mPlaceRepository.searchPlaces(mSearchText, mLocationText, mRadius, mType, mApiKey);
	}
}
