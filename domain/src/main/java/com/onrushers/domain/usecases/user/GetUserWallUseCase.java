package com.onrushers.domain.usecases.user;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.UserRepository;
import com.onrushers.domain.business.interactor.user.GetUserWallInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IFeedPagination;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetUserWallUseCase extends UseCase implements GetUserWallInteractor {

	private final AuthSessionRepository mAuthSessionRepository;
	private final UserRepository        mUserRepository;

	private Integer mUserId;
	private int mPage      = 1;
	private int mPageCount = 20;

	@Inject
	public GetUserWallUseCase(AuthSessionRepository authSessionRepository,
	                          UserRepository userRepository, ThreadExecutor threadExecutor,
	                          PostExecutionThread postExecutionThread) {

		super(threadExecutor, postExecutionThread);
		mAuthSessionRepository = authSessionRepository;
		mUserRepository = userRepository;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		return mAuthSessionRepository.getAuthSession()
				.flatMap(new Func1<IAuthSession, Observable<IFeedPagination>>() {
					@Override
					public Observable<IFeedPagination> call(IAuthSession authSession) {

						final Integer loggedUserId = authSession.getUserId();

						final int userId;
						if (mUserId != null) {
							userId = mUserId;
						} else {
							userId = loggedUserId;
						}

						return mUserRepository
							.getUserWall(userId, mPage, mPageCount, authSession.getToken())
							.map(new Func1<IFeedPagination, IFeedPagination>() {
								@Override
								public IFeedPagination call(IFeedPagination feedPagination) {

									for (IFeed feed : feedPagination.getItems()) {
										feed.compareWithUserId(loggedUserId);
									}

									Collections.sort(feedPagination.getItems(),
										new Comparator<IFeed>() {
											@Override
											public int compare(IFeed lhs, IFeed rhs) {
												/** if same dates, sort by feed type descending: 6, 5,.. and 1 */
												if (rhs.getCreatedAt().equals(lhs.getCreatedAt())) {
													return lhs.getType().getValue().compareTo(rhs.getType().getValue());
												}
												/** sort by dates descending */
												return rhs.getCreatedAt().compareTo(lhs.getCreatedAt());
											}
										});

									return feedPagination;
								}
							});
					}
				});
	}

	@Override
	public void setUserId(Integer userId) {
		mUserId = userId;
	}

	@Override
	public void setPage(int page) {
		mPage = page;
	}

	@Override
	public void setPageCount(int pageCount) {
		mPageCount = pageCount;
	}
}
