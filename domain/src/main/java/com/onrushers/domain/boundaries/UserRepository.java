package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.ICreateUserResult;
import com.onrushers.domain.business.model.IFeedPagination;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.FeedType;
import com.onrushers.domain.business.type.Gender;

import java.util.List;

import rx.Observable;

public interface UserRepository {

	Observable<ICreateUserResult> createUser(String username, String firstName, String lastName,
	                                         String email, String password, String birthDate,
	                                         Gender gender, String facebookId,
	                                         Integer profilePictureId);

	Observable<IUser> getUser(Integer id, String accessToken);

	Observable<List> getUsers(String userIds, String accessToken);

	Observable<IFeedPagination> getUserFeeds(int id, int page, int count, FeedType feedType, String accessToken);

	Observable<IFeedPagination> getUserWall(int id, int page, int count, String accessToken);

	Observable<IPagination> getUserHeros(int id, int page, int count, String accessToken);

	Observable<IPagination> getUserFans(int id, int page, int count, String accessToken);

	Observable<IPagination> getUserNotifications(int id, int page, int count, String accessToken);

	Observable<IUser> updateUserInfo(int id, String firstName, String lastName, String email,
	                                 Gender gender, String accessToken);

	Observable<IUser> updateUserFacebookInfo(int id, String facebookId, String accessToken);

	Observable<IUser> updateUserProfile(int id, String username, String description, String accessToken);

	Observable<IUser> updateUserAvatar(int id, Integer profilePictureId, String accessToken);

	Observable<IUser> updateUserCover(int id, Integer coverPictureId, String accessToken);

}
