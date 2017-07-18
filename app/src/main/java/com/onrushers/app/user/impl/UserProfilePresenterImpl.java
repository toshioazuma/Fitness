package com.onrushers.app.user.impl;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.onrushers.app.R;
import com.onrushers.app.file.Downloader;
import com.onrushers.app.user.UserProfilePresenter;
import com.onrushers.app.user.UserProfileView;
import com.onrushers.domain.business.interactor.auth_session.GetAuthSessionInteractor;
import com.onrushers.domain.business.interactor.relation.CreateRelationInteractor;
import com.onrushers.domain.business.interactor.relation.DeleteRelationInteractor;
import com.onrushers.domain.business.interactor.user.GetUserInteractor;
import com.onrushers.domain.business.model.IAuthSession;
import com.onrushers.domain.business.model.IGenericResult;
import com.onrushers.domain.business.model.IRelation;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.common.DefaultSubscriber;

import javax.inject.Inject;

public class UserProfilePresenterImpl implements UserProfilePresenter {

	private final Context    mContext;
	private final Resources  mResources;

	private final GetAuthSessionInteractor mGetAuthSessionInteractor;
	private final GetUserInteractor        mGetUserInteractor;
	private final CreateRelationInteractor mCreateRelationInteractor;
	private final DeleteRelationInteractor mDeleteRelationInteractor;

	private UserProfileView mView;

	private IUser   mUser         = null;
	private boolean mIsLoggedUser = false;

	@Inject
	public UserProfilePresenterImpl(Context context, GetAuthSessionInteractor getAuthSessionInteractor,
	                                GetUserInteractor getUserInteractor,
	                                CreateRelationInteractor createRelationInteractor,
	                                DeleteRelationInteractor deleteRelationInteractor) {
		mContext = context;
		mResources = context.getResources();
		mGetAuthSessionInteractor = getAuthSessionInteractor;
		mGetUserInteractor = getUserInteractor;
		mCreateRelationInteractor = createRelationInteractor;
		mDeleteRelationInteractor = deleteRelationInteractor;
	}

	//region UserProfilePresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setView(UserProfileView view) {
		mView = view;
	}

	@Override
	public void compareToAuthUser(final Integer userId) {
		mGetAuthSessionInteractor.execute(new DefaultSubscriber<IAuthSession>() {

			@Override
			public void onNext(IAuthSession authSession) {
				mIsLoggedUser = userId == authSession.getUserId();
				mView.showMyProfileControls(mIsLoggedUser);
			}
		});
	}

	@Override
	public void setPresentingLoggedInUser(boolean isPresentingLoggedInUser) {
		mIsLoggedUser = isPresentingLoggedInUser;
	}

	@Override
	public boolean isPresentingLoggedInUser() {
		return mIsLoggedUser;
	}

	@Override
	public void loadUserById(Integer userId) {
		mGetUserInteractor.setUserId(userId);
		mGetUserInteractor.execute(new GetUserSubscriber());
	}

	@Override
	public void presentUser(IUser user) {
		if (user == null) {
			return;
		}

		mView.showUserName(user.getUsername());
		mView.showUserGrade(user.getGradeString());

		if (TextUtils.isEmpty(user.getDescription())) {
			mView.hideDescription();
		} else {
			mView.showDescription(user.getDescription());
		}

		if (!TextUtils.isEmpty(user.getProfilePicture())) {
			String fileUrl = Downloader.Companion.getInstance().resourceUrl(user.getProfilePicture());
			mView.showAvatarPictureUrl(fileUrl);
		}

		if (!TextUtils.isEmpty(user.getCoverPicture())) {
			String fileUrl = Downloader.Companion.getInstance().resourceUrl(user.getCoverPicture());
			mView.showCoverPictureUrl(fileUrl);
		}

		if (user.getHerosCount() != null) {
			int herosCount = user.getHerosCount();
			String heroString = mResources.getQuantityString(R.plurals.user_profile_hero_count, herosCount, herosCount);
			mView.showHeroCount(getSpannableString(herosCount, heroString));
		}

		if (user.getFansCount() != null) {
			int fansCount = user.getFansCount();
			String fanString = mResources.getQuantityString(R.plurals.user_profile_fan_count, fansCount, fansCount);
			mView.showFanCount(getSpannableString(fansCount, fanString));
		}

		if (!mIsLoggedUser) {
			if (user.getHeroRelation() != null) {
				mView.showFollowingState();
			} else {
				mView.showFollowState();
			}
		}
	}

	@Override
	public boolean canShowHerosList() {
		if (mUser != null) {
			return mUser.getHerosCount() > 0;
		}
		return false;
	}

	@Override
	public boolean canShowFansList() {
		if (mUser != null) {
			return mUser.getFansCount() > 0;
		}
		return false;
	}

	@Override
	public void toggleFollowUser() {
		mView.showLoading();

		if (mUser.getHeroRelation() == null) {
			mCreateRelationInteractor.setHero(mUser);
			mCreateRelationInteractor.execute(new DefaultSubscriber<IRelation>() {
				@Override
				public void onError(Throwable e) {
					mView.hideLoading();
				}

				@Override
				public void onNext(IRelation relation) {
					mGetUserInteractor.execute(new GetUserSubscriber());
				}
			});
		} else {
			mDeleteRelationInteractor.setRelation(mUser.getHeroRelation());
			mDeleteRelationInteractor.execute(new DefaultSubscriber<IGenericResult>() {
				@Override
				public void onError(Throwable e) {
					mView.hideLoading();
				}

				@Override
				public void onNext(IGenericResult result) {
					mGetUserInteractor.execute(new GetUserSubscriber());
				}
			});
		}
	}

	@Override
	public IUser getPresentedUser() {
		return mUser;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	private final class GetUserSubscriber extends DefaultSubscriber<IUser> {
		@Override
		public void onError(Throwable e) {
			mView.hideLoading();
		}

		@Override
		public void onNext(IUser user) {
			mView.hideLoading();
			mUser = user;
			presentUser(user);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Private
	//----------------------------------------------------------------------------------------------

	private SpannableString getSpannableString(int count, String string) {
		int colorLength = String.valueOf(count).length();
		SpannableString span = new SpannableString(string);

		int color = ContextCompat.getColor(mContext, R.color.blueSecondary);

		span.setSpan(new ForegroundColorSpan(color), 0, colorLength,
			Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		span.setSpan(new RelativeSizeSpan(1.2f), 0, colorLength,
			Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		return span;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
