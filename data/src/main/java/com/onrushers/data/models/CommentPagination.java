package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.ICommentPagination;

import java.util.List;

public class CommentPagination implements ICommentPagination {

	@Expose
	public int count;

	@Expose
	public int pages;

	@Expose
	@SerializedName("list")
	public List<Comment> comments;


	//region ICommentPagination
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
	public List<? extends IComment> getItems() {
		return comments;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
