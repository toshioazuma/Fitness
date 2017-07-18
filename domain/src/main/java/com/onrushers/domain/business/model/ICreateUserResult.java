package com.onrushers.domain.business.model;

import java.util.List;

public interface ICreateUserResult {

	boolean isSuccess();

	List<String[]> getErrorMessages();

	String getToken();

	Integer getUserId();

}
