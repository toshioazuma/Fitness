package com.onrushers.domain.business.model;

import java.util.List;

public interface ILoginUserResult {

	boolean isSuccess();

	Integer getStatusCode();

	List<String[]> getErrorMessages();

	String getToken();

	Integer getUserId();
}
