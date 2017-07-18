package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IBoostPagination;

import java.util.List;

public class BoostPagination implements IBoostPagination {

	@Expose
	public int count;

	@Expose
	public int pages;

	@Expose
	@SerializedName("list")
	public List<Boost> boosts;


	//region IBoostPagination
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
	public List<? extends IBoost> getItems() {
		return boosts;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
