package com.onrushers.app.feed.create;

import com.onrushers.domain.business.model.IUser;

import java.io.File;
import java.util.List;

public interface FeedCreatePresenter {

	void setView(FeedCreateView view);

	void setText(String text);

	void setPlace(String place);

	void setTagUsers(List<IUser> users);

	void postPicture(File file);

	void postFeed();
}
