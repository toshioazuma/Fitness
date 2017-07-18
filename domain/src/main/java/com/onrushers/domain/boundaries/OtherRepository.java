package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.ISliderResult;

import rx.Observable;

public interface OtherRepository {

	Observable<ISliderResult> getSlider(String accessToken);
}
