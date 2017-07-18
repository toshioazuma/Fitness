package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.IGenericResult;

import rx.Observable;

public interface ResetPasswordRepository {

	Observable<IGenericResult> resetPassword(String username);
}
