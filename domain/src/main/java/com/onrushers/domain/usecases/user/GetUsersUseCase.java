package com.onrushers.domain.usecases.user;

import android.text.TextUtils;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.UserRepository;
import com.onrushers.domain.business.interactor.user.GetUsersInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetUsersUseCase extends UseCase implements GetUsersInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final UserRepository        mUserRepository;

	private final static String SEPARATOR = ",";

	private String mUserIds = "";


	@Inject
	public GetUsersUseCase(AuthSessionRepository authSessionRepository,
	                       UserRepository userRepository, ThreadExecutor threadExecutor,
	                       PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mUserRepository = userRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<List>>() {
				@Override
				public Observable<List> call(IAuthSession authSession) {

					if (mUserIds.contains(SEPARATOR)) {
						return mUserRepository.getUsers(mUserIds, authSession.getToken());
					} else {
						return mUserRepository.getUser(Integer.valueOf(mUserIds), authSession.getToken())
							.map(new Func1<IUser, List>() {
								@Override
								public List call(IUser user) {
									List<IUser> list = new ArrayList<>();
									list.add(user);
									return list;
								}
							});
					}
				}
			});
	}

	@Override
	public void setUserIds(List<Integer> userIds) {
		if (userIds == null || userIds.isEmpty()) {
			return;
		}
		if (userIds.size() == 1) {
			setUserId(userIds.get(0));
		} else {
			mUserIds = TextUtils.join(SEPARATOR, userIds);
		}
	}

	@Override
	public void setUserId(Integer userId) {
		mUserIds = String.valueOf(userId);
	}
}
