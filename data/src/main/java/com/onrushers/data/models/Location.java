package com.onrushers.data.models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.onrushers.domain.business.model.ILocation;

public class Location implements ILocation {

	@Expose
	public int id;

	@Expose
	public double latitude;

	@Expose
	public double longitude;

	@Expose
	public String country;

	@Expose
	public String city;

	@Expose
	public String zipcode;

	@Expose
	public String address;

	@Expose
	public String region;

	//region Parcelable
	//----------------------------------------------------------------------------------------------

	public Location() {

	}

	public Location(Parcel in) {
		id = in.readInt();
		latitude = in.readDouble();
		longitude = in.readDouble();
		country = in.readString();
		city = in.readString();
		zipcode = in.readString();
		address = in.readString();
		region = in.readString();
	}

	public static final Creator<Location> CREATOR = new Creator<Location>() {
		@Override
		public Location createFromParcel(Parcel in) {
			return new Location(in);
		}

		@Override
		public Location[] newArray(int size) {
			return new Location[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeString(country);
		dest.writeString(city);
		dest.writeString(zipcode);
		dest.writeString(address);
		dest.writeString(region);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region ILocation
	//----------------------------------------------------------------------------------------------

	@Override
	public int getId() {
		return id;
	}

	@Override
	public double getLatitude() {
		return latitude;
	}

	@Override
	public double getLongitude() {
		return longitude;
	}

	@Override
	public String getCountry() {
		return country;
	}

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public String getZipcode() {
		return zipcode;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public String getRegion() {
		return region;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
