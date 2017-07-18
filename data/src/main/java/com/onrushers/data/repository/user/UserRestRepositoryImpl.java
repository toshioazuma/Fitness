package com.onrushers.data.repository.user;

import com.onrushers.data.api.service.UserService;
import com.onrushers.data.models.User;
import com.onrushers.domain.boundaries.UserRepository;
import com.onrushers.domain.business.model.ICreateUserResult;
import com.onrushers.domain.business.model.IFeedPagination;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.FeedType;
import com.onrushers.domain.business.type.Gender;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class UserRestRepositoryImpl implements UserRepository {

	private final UserService mUserService;

	@Inject
	public UserRestRepositoryImpl(UserService userService) {
		mUserService = userService;
	}

	@Override
	public Observable<ICreateUserResult> createUser(String username, String firstName,
	                                                String lastName, String email, String password,
	                                                String birthDate, Gender gender,
	                                                String facebookId, Integer profilePictureId) {
		final User user = new User();
		user.username = username;
		user.firstName = firstName;
		user.lastName = lastName;
		user.email = email;
		user.password = password;
		user.birthDate = birthDate;
		user.gender = gender.getValue();

		if (facebookId != null) {
			user.facebookId = facebookId;
		}
		if (profilePictureId != null) {
			user.photoId = String.valueOf(profilePictureId);
		}

		return mUserService.postUser(user).cast(ICreateUserResult.class);
	}

	@Override
	public Observable<IUser> getUser(Integer id, String accessToken) {
		return mUserService.getUser(id, accessToken).cast(IUser.class);
	}

	@Override
	public Observable<List> getUsers(String userIds, String accessToken) {
		return mUserService.getUsers(userIds, accessToken).cast(List.class);
	}

	@Override
	public Observable<IFeedPagination> getUserFeeds(int id, int page, int count, FeedType type, String accessToken) {
		Integer feedType = null;
		if (type != FeedType.Any) {
			feedType = type.getValue();
		}

		return mUserService.getUserFeeds(id, page, count, feedType, accessToken).cast(IFeedPagination.class);
	}

	@Override
	public Observable<IFeedPagination> getUserWall(int id, int page, int count, String accessToken) {
		return mUserService.getUserWall(id, page, count, accessToken).cast(IFeedPagination.class);
	}

	@Override
	public Observable<IPagination> getUserHeros(int id, int page, int count, String accessToken) {
		return mUserService.getUserHeros(id, page, count, accessToken).cast(IPagination.class);
	}

	@Override
	public Observable<IPagination> getUserFans(int id, int page, int count, String accessToken) {
		return mUserService.getUserFans(id, page, count, accessToken).cast(IPagination.class);
	}

	@Override
	public Observable<IPagination> getUserNotifications(int id, int page, int count, String accessToken) {
		return mUserService.getUserNotifications(id, page, count, accessToken).cast(IPagination.class);
	}

	@Override
	public Observable<IUser> updateUserInfo(int id, String firstName, String lastName, String email, Gender gender, String accessToken) {

		final User user = new User();
		user.id = id;
		user.firstName = firstName;
		user.lastName = lastName;
		user.email = email;
		user.gender = gender.getValue();

		return mUserService.putUser(user, accessToken).cast(IUser.class);
	}

	@Override
	public Observable<IUser> updateUserFacebookInfo(int id, String facebookId, String accessToken) {
		final User user = new User();
		user.id = id;
		user.facebookId = facebookId;

		return mUserService.putUser(user, accessToken).cast(IUser.class);
	}

	@Override
	public Observable<IUser> updateUserProfile(int id, String username, String description, String accessToken) {

		User user = new User();
		user.id = id;
		user.username = username;
		user.description = description;

		return mUserService.putUser(user, accessToken).cast(IUser.class);
	}

	@Override
	public Observable<IUser> updateUserAvatar(int id, Integer profilePictureId, String accessToken) {

		User user = new User();
		user.id = id;
		user.photoId = String.valueOf(profilePictureId);

		return mUserService.putUser(user, accessToken).cast(IUser.class);
	}

	@Override
	public Observable<IUser> updateUserCover(int id, Integer coverPictureId, String accessToken) {

		User user = new User();
		user.id = id;
		user.coverPictureId = String.valueOf(coverPictureId);

		return mUserService.putUser(user, accessToken).cast(IUser.class);
	}
}
