package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.onrushers.domain.business.model.IGenericError;
import com.onrushers.domain.business.model.IGenericResult;

public class GenericResult implements IGenericResult {

	@Expose
	public boolean result = false;

	@Expose
	public GenericError error = null;


	@Override
	public boolean isSuccess() {
		return result;
	}

	@Override
	public IGenericError getError() {
		return error;
	}
}
