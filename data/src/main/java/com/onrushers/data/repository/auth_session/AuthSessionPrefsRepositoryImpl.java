package com.onrushers.data.repository.auth_session;

import android.content.Context;
import android.content.SharedPreferences;

import com.onrushers.data.internal.di.modules.DataModule;
import com.onrushers.data.models.ProxyAuthSession;
import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.business.model.IAuthSession;

import javax.inject.Inject;

import rx.Observable;

public class AuthSessionPrefsRepositoryImpl implements AuthSessionRepository {

	public static final String PREFS_NAME = "OnRushers_Prefs";

	private static final String KEY_TOKEN   = "k.token";
	private static final String KEY_USER_ID = "k.user_id";

	private final SharedPreferences mPrefs;

	@Inject
	public AuthSessionPrefsRepositoryImpl() {
		mPrefs = DataModule.CONTEXT.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}

	@Override
	public Observable<Boolean> hasActiveAuthSession() {
		return Observable.just((mPrefs.getString(KEY_TOKEN, null) != null));
	}

	@Override
	public Observable<IAuthSession> getAuthSession() {
		String token = mPrefs.getString(KEY_TOKEN, null);
		Integer userId = mPrefs.getInt(KEY_USER_ID, 0);

		if (token == null || userId == 0) {
			return Observable.just(null);
		}

		return Observable.just((IAuthSession) new ProxyAuthSession(token, userId));
	}

	@Override
	public Observable<Boolean> saveAuthSession(String token, Integer userId) {
		boolean result = mPrefs.edit()
			.putString(KEY_TOKEN, token)
			.putInt(KEY_USER_ID, userId)
			.commit();

		return Observable.just(result);
	}

	@Override
	public Observable<Boolean> destroyAuthSession() {
		boolean result = mPrefs.edit().clear().commit();
		return Observable.just(result);
	}
}
