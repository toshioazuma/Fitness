package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.onrushers.domain.business.model.IFile;

public class File implements IFile {

	@Expose
	public Integer id;

	@Expose
	public String path;


	//region IFile
	//----------------------------------------------------------------------------------------------

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getPath() {
		return path;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
