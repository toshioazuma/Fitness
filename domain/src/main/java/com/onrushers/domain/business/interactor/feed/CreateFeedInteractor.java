package com.onrushers.domain.business.interactor.feed;

import com.onrushers.domain.business.interactor.Interactor;
import com.onrushers.domain.business.model.IUser;

import java.util.List;

public interface CreateFeedInteractor extends Interactor {

	void setContent(String content);

	void setPlace(String place);

	void setTagUsers(List<IUser> users);

	void addPhoto(int photoId);
}
