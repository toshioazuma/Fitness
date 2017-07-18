package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.onrushers.domain.business.model.IGenericError;

public class GenericError implements IGenericError {

	@Expose
	public int code;

	@Expose
	public String message;


	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
