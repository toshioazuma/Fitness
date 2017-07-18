package com.onrushers.data.repository.auth_session;

import com.onrushers.data.db.entity.AuthSessionEntity;
import com.onrushers.data.models.ProxyAuthSession;
import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.business.model.IAuthSession;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class AuthSessionDBRepositoryImpl implements AuthSessionRepository {

	private static final boolean FAKE = false;

	private IAuthSession mCacheAuthSession;

	@Inject
	public AuthSessionDBRepositoryImpl() {

	}

	@Override
	public Observable<Boolean> hasActiveAuthSession() {

		return Realm.getDefaultInstance()
			.asObservable()
			.flatMap(new Func1<Realm, Observable<Boolean>>() {
				@Override
				public Observable<Boolean> call(Realm realm) {
					AuthSessionEntity existingAuthSession = realm.where(AuthSessionEntity.class).findFirst();
					return Observable.just((existingAuthSession != null));
				}
			});

		/**
		 return Observable.create(new Observable.OnSubscribe<Boolean>() {
		@Override public void call(Subscriber<? super Boolean> subscriber) {

		final Realm realm = Realm.getDefaultInstance();

		final AuthSessionEntity existingAuthSession = realm
		.where(AuthSessionEntity.class).findFirst();

		boolean result = (existingAuthSession != null);

		if (FAKE) {
		subscriber.onNext(true);
		} else {
		subscriber.onNext(result);
		}
		realm.close();
		}
		});
		 */
	}

	@Override
	public Observable<IAuthSession> getAuthSession() {

		if (mCacheAuthSession != null) {
			return Observable.just(mCacheAuthSession);
		}

		return Realm.getDefaultInstance()
			.asObservable()
			.flatMap(new Func1<Realm, Observable<IAuthSession>>() {
				@Override
				public Observable<IAuthSession> call(Realm realm) {

					AuthSessionEntity existingAuthSession = realm
						.where(AuthSessionEntity.class).findFirst();

					mCacheAuthSession = realm.copyFromRealm(existingAuthSession);
					realm.close();

					return Observable.just(mCacheAuthSession);
				}
			});

		/**
		 return Observable.create(new Observable.OnSubscribe<IAuthSession>() {
		@Override public void call(Subscriber<? super IAuthSession> subscriber) {

		final Realm realm = Realm.getDefaultInstance();

		final AuthSessionEntity existingAuthSession = realm
		.where(AuthSessionEntity.class).findFirst();

		if (FAKE) {
		AuthSessionEntity fakeAuthSessionEntity = new AuthSessionEntity();
		fakeAuthSessionEntity.setUserId(3);
		fakeAuthSessionEntity.setToken("MTE3NzY1MGI5Mjg3Y2E3YmQyYzdhMDhlYzcyNTk1ZWY5NTM5ZjQ2ZTNmZWQ1YjhlYmE1MWVjNzkxMDRmY2MyOQ");

		subscriber.onNext(fakeAuthSessionEntity);
		} else {
		subscriber.onNext(new ProxyAuthSession(existingAuthSession));
		}
		realm.close();
		}
		});
		 */
	}

	@Override
	public Observable<Boolean> saveAuthSession(final String token, final Integer userId) {

		return Observable.create(new Observable.OnSubscribe<Boolean>() {
			@Override
			public void call(Subscriber<? super Boolean> subscriber) {
				final Realm realm = Realm.getDefaultInstance();
				realm.beginTransaction();

				/**
				 * Delete old AuthSession entries (one normally).
				 */
				realm.where(AuthSessionEntity.class)
					.findAll()
					.deleteAllFromRealm();

				/**
				 * Create and persist the new AuthSession entry.
				 */
				AuthSessionEntity entity = realm.createObject(AuthSessionEntity.class);
				entity.setToken(token);
				entity.setUserId(userId);
				realm.commitTransaction();

				subscriber.onNext(true);
				realm.close();
			}
		});
	}

	@Override
	public Observable<Boolean> destroyAuthSession() {

		return Observable.create(new Observable.OnSubscribe<Boolean>() {
			@Override
			public void call(Subscriber<? super Boolean> subscriber) {
				final Realm realm = Realm.getDefaultInstance();
				realm.beginTransaction();

				/**
				 * Delete old AuthSession entries (one normally).
				 */
				realm.where(AuthSessionEntity.class)
					.findAll()
					.deleteAllFromRealm();

				realm.commitTransaction();

				subscriber.onNext(true);
				realm.close();
			}
		});
	}
}
