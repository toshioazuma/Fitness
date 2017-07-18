package com.onrushers.data.repository.authentication;

import com.onrushers.data.api.service.AuthenticationService;
import com.onrushers.data.models.Login;
import com.onrushers.data.models.PostLoginError;
import com.onrushers.data.models.PostLoginResult;
import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.AuthenticationRepository;
import com.onrushers.domain.business.model.ILoginUserResult;
import com.onrushers.domain.common.DefaultSubscriber;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

public class AuthenticationRestRepositoryImpl implements AuthenticationRepository {

	private final AuthenticationService mAuthenticationService;
	private final AuthSessionRepository mAuthSessionRepository;


	@Inject
	public AuthenticationRestRepositoryImpl(AuthenticationService service,
	                                        AuthSessionRepository repository) {
		mAuthenticationService = service;
		mAuthSessionRepository = repository;
	}

	@Override
	public Observable<ILoginUserResult> loginUser(String username, String password) {

		final Login loginObject = new Login();
		loginObject.username = username;
		loginObject.password = password;

		return login(loginObject);
	}

	@Override
	public Observable<ILoginUserResult> loginFacebookUser(String facebookId) {

		final Login loginObject = new Login();
		loginObject.facebookId = facebookId;

		return login(loginObject);
	}

	private final Observable<ILoginUserResult> login(final Login loginObject) {

		return Observable.create(new Observable.OnSubscribe<ILoginUserResult>() {

			@Override
			public void call(final Subscriber<? super ILoginUserResult> subscriber) {

				mAuthenticationService
					.loginUser(loginObject)
					.enqueue(new Callback<PostLoginResult>() {

						@Override
						public void onResponse(
							Call<PostLoginResult> call, Response<PostLoginResult> response) {

							final PostLoginResult result = response.body();

							if (result == null) {
								/**
								 * Case of 404.
								 */
								subscriber.onNext(new PostLoginError(response.code()));

							} else if (result.isSuccess()) {
								/**
								 * Case of success:
								 * Store user token and user id.
								 */
								mAuthSessionRepository
									.saveAuthSession(result.getToken(), result.getUserId())
									.subscribe(new DefaultSubscriber<Boolean>() {

										@Override
										public void onNext(Boolean aBoolean) {
											subscriber.onNext(result);
										}
									});

							} else {
								/**
								 * Case of success but with error message.
								 */
								subscriber.onNext(result);
							}
						}

						@Override
						public void onFailure(Call<PostLoginResult> call, Throwable t) {
							subscriber.onError(t);
						}
					});
			}
		});
	}
}
