package com.onrushers.domain.usecases.feed;

import com.onrushers.domain.boundaries.AuthSessionRepository;
import com.onrushers.domain.boundaries.FeedRepository;
import com.onrushers.domain.business.interactor.feed.CreateFeedInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IRelation;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.FeedType;
import com.onrushers.domain.common.DefaultSubscriber;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;
import com.onrushers.domain.usecases.UseCase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class CreateFeedUseCase extends UseCase implements CreateFeedInteractor {

	private static final String TAG = "CreateFeedUseCase";

	private final FeedRepository        mRepository;
	private final AuthSessionRepository mAuthSessionRepository;

	private int           mType       = FeedType.Post.getValue();
	private String        mContent    = "";
	private String        mPlace      = null;
	private List<Integer> mTagUserIds = null;
	private List<Integer> mPhotos     = null;

	@Inject
	public CreateFeedUseCase(FeedRepository repository, AuthSessionRepository authSessionRepository,
	                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
		super(threadExecutor, postExecutionThread);
		mRepository = repository;
		mAuthSessionRepository = authSessionRepository;
		mPhotos = new ArrayList<>();
	}

	@Override
	protected Observable buildUseCaseObservable() {

		return mAuthSessionRepository.getAuthSession()
			.flatMap(new Func1<IAuthSession, Observable<IFeed>>() {
				@Override
				public Observable<IFeed> call(IAuthSession authSession) {

					final Integer userId = authSession.getUserId();
					final String accessToken = authSession.getToken();

					return mRepository.createFeed(mType, userId, mContent, mPlace, mTagUserIds,
						mPhotos, accessToken);
				}
			});
	}

	@Override
	public void setContent(String content) {
		mContent = content;
	}

	@Override
	public void setPlace(String place) {
		mPlace = place;
	}

	@Override
	public void setTagUsers(List<IUser> users) {
		if (users == null || users.isEmpty()) {
			mTagUserIds = null;
		} else {
			Observable.from(users)
				.map(new Func1<IUser, Integer>() {
					@Override
					public Integer call(IUser user) {
						return user.getId();
					}
				})
				.subscribe(new DefaultSubscriber<Integer>() {
					@Override
					public void onStart() {
						mTagUserIds = new ArrayList<>();
					}

					@Override
					public void onNext(Integer id) {
						mTagUserIds.add(id);
					}
				});
		}
	}

	@Override
	public void addPhoto(int photoId) {
		if (mPhotos == null) {
			mPhotos = new ArrayList<>();
		}
		mPhotos.add(photoId);
	}
}
