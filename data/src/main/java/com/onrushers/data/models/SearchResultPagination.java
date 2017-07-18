package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.ISearchResult;
import com.onrushers.domain.business.model.ISearchResultPagination;

import java.util.List;

public class SearchResultPagination implements ISearchResultPagination {

	@Expose
	public int count;

	@Expose
	public int pages;

	@Expose
	@SerializedName("list")
	public List<ResultResultItem> items;


	//region ISearchResultPagination
	//----------------------------------------------------------------------------------------------

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public int getPages() {
		return pages;
	}

	@Override
	public List<? extends ISearchResult> getItems() {
		return items;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
