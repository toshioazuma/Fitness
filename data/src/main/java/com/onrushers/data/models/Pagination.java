package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.onrushers.domain.business.model.IPagination;

import java.util.List;

public class Pagination<T> implements IPagination {

	@Expose
	public int count;

	@Expose
	public int pages;

	@Expose
	public List<T> list;


	//region IPagination
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
	public List<? extends T> getItems() {
		return list;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
