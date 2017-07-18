package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.ILoginUserResult;
import com.onrushers.domain.business.model.LoginUserResult;

import rx.Observable;

public interface AuthenticationRepository {

	Observable<ILoginUserResult> loginUser(String username, String password);

	Observable<ILoginUserResult> loginFacebookUser(String facebookId);
}
