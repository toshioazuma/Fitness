package com.onrushers.domain.business.model;

import java.util.List;

@Deprecated
public interface LoginUserResult {

	boolean isSuccess();

	List<String[]> getErrorMessages();

	String getToken();

	Integer getUserId();

}
