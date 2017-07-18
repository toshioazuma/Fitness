package com.onrushers.data.repository.other;

import com.onrushers.data.api.service.OtherService;
import com.onrushers.domain.boundaries.OtherRepository;
import com.onrushers.domain.business.model.ISliderResult;

import javax.inject.Inject;

import rx.Observable;

public class OtherApiRepositoryImpl implements OtherRepository {

	private final OtherService mOtherService;

	@Inject
	public OtherApiRepositoryImpl(OtherService otherService) {
		mOtherService = otherService;
	}

	@Override
	public Observable<ISliderResult> getSlider(String accessToken) {

		return mOtherService.getPhotosSlider(accessToken).cast(ISliderResult.class);
	}
}
