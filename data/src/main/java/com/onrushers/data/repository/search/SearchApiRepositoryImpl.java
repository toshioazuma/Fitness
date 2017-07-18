package com.onrushers.data.repository.search;

import com.onrushers.data.api.service.SearchService;
import com.onrushers.domain.boundaries.SearchRepository;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.SearchResultType;

import javax.inject.Inject;

import rx.Observable;

public class SearchApiRepositoryImpl implements SearchRepository {

	private final SearchService mSearchService;

	@Inject
	public SearchApiRepositoryImpl(SearchService searchService) {
		mSearchService = searchService;
	}

	@Override
	public Observable<IPagination> getSearch(String query, SearchResultType type, int page, int count, String accessToken) {
		final SearchResultType searchType;
		if (type != null) {
			searchType = type;
		} else {
			searchType = SearchResultType.Any;
		}

		return mSearchService
			.getSearch(query, searchType.getValue(), page, count, accessToken)
			.cast(IPagination.class);
	}

	@Override
	public Observable<IUser> searchUser(String userSlug) {

		return mSearchService
			.searchUser(userSlug)
			.cast(IUser.class);
	}
}
