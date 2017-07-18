package com.onrushers.data.repository.reset;

import com.onrushers.data.api.service.ResetPasswordService;
import com.onrushers.domain.boundaries.ResetPasswordRepository;
import com.onrushers.domain.business.model.IGenericResult;

import javax.inject.Inject;

import rx.Observable;

public class ResetPasswordApiRepositoryImpl implements ResetPasswordRepository {

	private final ResetPasswordService mResetPasswordService;

	@Inject
	public ResetPasswordApiRepositoryImpl(ResetPasswordService resetPasswordService) {
		mResetPasswordService = resetPasswordService;
	}

	@Override
	public Observable<IGenericResult> resetPassword(String username) {

		return mResetPasswordService
			.resetPassword(username)
			.cast(IGenericResult.class);
	}
}
