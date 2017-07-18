package com.onrushers.domain.business.interactor.comment;

import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.interactor.Interactor;

public interface DeleteCommentInteractor extends Interactor {

	void setComment(IComment comment);
}
