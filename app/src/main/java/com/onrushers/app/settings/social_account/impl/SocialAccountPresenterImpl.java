package com.onrushers.app.settings.social_account.impl;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.onrushers.app.R;
import com.onrushers.app.settings.social_account.SocialAccountPresenter;
import com.onrushers.app.settings.social_account.SocialAccountView;
import com.onrushers.domain.business.interactor.user.GetUserInteractor;
import com.onrushers.domain.business.interactor.user.UpdateUserInfoInteractor;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.common.DefaultSubscriber;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

public class SocialAccountPresenterImpl implements SocialAccountPresenter {

	private SocialAccountView mView;

	@Inject
	GetUserInteractor mGetUserInteractor;

	@Inject
	UpdateUserInfoInteractor mUpdateUserInfoInteractor;

	@Inject
	public SocialAccountPresenterImpl() {

	}

	//region Object life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void setView(@NotNull SocialAccountView view) {
		mView = view;
	}

	@Override
	public void onViewCreated() {
		mGetUserInteractor.execute(new GetUserSubscriber());
	}

	@Override
	public void destroyView() {
		mGetUserInteractor.unsubscribe();
		mUpdateUserInfoInteractor.unsubscribe();

		mView = null;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Event handling
	//----------------------------------------------------------------------------------------------

	@Override
	public void linkFacebookProfile(@NotNull String facebookId) {
		mUpdateUserInfoInteractor.setFacebookId(facebookId);
		mUpdateUserInfoInteractor.execute(new UpdateUserSubscriber());
	}

	@Override
	public void unlinkFacebookProfile() {
		mView.showFacebookUserName(null);
		mUpdateUserInfoInteractor.setFacebookId("");
		mUpdateUserInfoInteractor.execute(new UpdateUserSubscriber());
	}

	@Override
	public void presentFacebookUserName(@NotNull Context context, @Nullable String username) {

		if (username == null) {
			username = " -";
		}

		String usernameText = context.getString(R.string.social_account_logged_in_as_facebook, username);
		SpannableString span = new SpannableString(usernameText);
		span.setSpan(new StyleSpan(Typeface.BOLD), 0, usernameText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		int start = usernameText.indexOf(username);
		span.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.blue)),
			start, start + username.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		mView.showFacebookUserName(span);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	private class GetUserSubscriber extends DefaultSubscriber<IUser> {

		@Override
		public void onNext(IUser user) {
			if (!TextUtils.isEmpty(user.getFacebookId())) {
				mView.performFetchFacebookUserId();
			}
		}
	}

	private class UpdateUserSubscriber extends DefaultSubscriber<IUser> {

	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
