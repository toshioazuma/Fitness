package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IRelation;

import java.util.Date;

public class Relation implements IRelation {

	@Expose
	public Integer id;

	@Expose
	@SerializedName("fan_id")
	public Integer fanId;

	@Expose
	@SerializedName("hero_id")
	public Integer heroId;

	@Expose
	public Date date;


	//region IRelation
	//----------------------------------------------------------------------------------------------

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public Integer getFanId() {
		return fanId;
	}

	@Override
	public Integer getHeroId() {
		return heroId;
	}

	@Override
	public Date getDate() {
		return date;
	}

	//----------------------------------------------------------------------------------------------
	//endregion


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder
			.append(super.toString())
			.append(": { ")
			.append("id: ").append(id)
			.append(", fan_id: ").append(fanId)
			.append(", hero_id: ").append(heroId)
			.append(" }");

		return builder.toString();
	}
}
