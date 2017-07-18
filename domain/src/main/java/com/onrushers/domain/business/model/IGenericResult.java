package com.onrushers.domain.business.model;

public interface IGenericResult {

	boolean isSuccess();

	IGenericError getError();
}
