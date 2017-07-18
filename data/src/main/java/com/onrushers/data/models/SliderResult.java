package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.onrushers.domain.business.model.ISliderResult;

import java.util.List;

public class SliderResult implements ISliderResult {

	@Expose
	public List<Integer> photos;

	//region ISliderResult
	//----------------------------------------------------------------------------------------------

	@Override
	public List<Integer> getPhotos() {
		return photos;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
