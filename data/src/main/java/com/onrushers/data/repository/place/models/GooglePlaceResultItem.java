package com.onrushers.data.repository.place.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IPlaceResultItem;

import java.util.List;

public class GooglePlaceResultItem implements IPlaceResultItem {

	@Expose
	String id;

	@Expose
	String name;

	@Expose
	String icon;

	@Expose
	@SerializedName("place_id")
	String placeId;

	@Expose
	List<String> types;

	@Expose
	Geometry geometry;

	class Geometry {

		@Expose
		Location location;
	}

	class Location {

		@Expose
		@SerializedName("lat")
		Double latitude;

		@Expose
		@SerializedName("lng")
		Double longitude;
	}

	@Override
	public String getName() {
		return name;
	}
}
