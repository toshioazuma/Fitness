package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.onrushers.domain.business.model.IReport;

public class Report implements IReport {

	@Expose
	public Integer id;


	@Override
	public Integer getId() {
		return id;
	}
}
