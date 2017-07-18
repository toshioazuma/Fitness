package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.IAuthSession;

import rx.Observable;

public interface AuthSessionRepository {

	Observable<Boolean> hasActiveAuthSession();

	Observable<IAuthSession> getAuthSession();

	Observable<Boolean> saveAuthSession(String token, Integer userId);

	Observable<Boolean> destroyAuthSession();
}
