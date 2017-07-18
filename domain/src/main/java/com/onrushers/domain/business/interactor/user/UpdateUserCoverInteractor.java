package com.onrushers.domain.business.interactor.user;

import com.onrushers.domain.business.interactor.Interactor;

public interface UpdateUserCoverInteractor extends Interactor {

	void setCoverPictureId(Integer profilePictureId);
}
