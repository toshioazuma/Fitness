package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.SearchResultType;

import rx.Observable;

public interface SearchRepository {

	Observable<IPagination> getSearch(String query, SearchResultType type, int page, int count, String accessToken);

	Observable<IUser> searchUser(String userSlug);
}
