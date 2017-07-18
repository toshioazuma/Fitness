package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.onrushers.domain.business.model.IUploadResult;

public class UploadResult implements IUploadResult {

	@Expose
	public boolean result;

	@Expose
	public Integer id;

	@Expose
	public String message;

	//region IUploadResult
	//----------------------------------------------------------------------------------------------

	@Override
	public boolean isSuccess() {
		return result;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getMessage() {
		return message;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
