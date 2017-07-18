package com.onrushers.app.user.edit.impl;

import android.util.Log;

import com.onrushers.app.R;
import com.onrushers.app.common.bus.events.UserUpdateEvent;
import com.onrushers.app.file.FileClient;
import com.onrushers.app.user.edit.UserEditPresenter;
import com.onrushers.app.user.edit.UserEditView;
import com.onrushers.domain.bus.BusProvider;
import com.onrushers.domain.business.interactor.file.UploadFileInteractor;
import com.onrushers.domain.business.interactor.user.UpdateUserAvatarInteractor;
import com.onrushers.domain.business.interactor.user.UpdateUserCoverInteractor;
import com.onrushers.domain.business.interactor.user.UpdateUserProfileInteractor;
import com.onrushers.domain.business.model.IUploadResult;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.common.DefaultSubscriber;

import java.io.File;

import javax.inject.Inject;

public class UserEditPresenterImpl implements UserEditPresenter {

	private static final String TAG = "UserEditPresenterIMPL";

	private static final int PICTURE_TYPE_COVER  = 10;
	private static final int PICTURE_TYPE_AVATAR = 11;

	private final UploadFileInteractor        mUploadFileInteractor;
	private final UpdateUserProfileInteractor mUpdateUserProfileInteractor;
	private final UpdateUserAvatarInteractor  mUpdateUserAvatarInteractor;
	private final UpdateUserCoverInteractor   mUpdateUserCoverInteractor;

	private final FileClient mFileClient;

	private UserEditView mView;

	private Integer mNewProfilePictureId = null;
	private Integer mNewCoverPictureId   = null;

	@Inject
	public UserEditPresenterImpl(UploadFileInteractor uploadFileInteractor,
	                             UpdateUserProfileInteractor updateUserProfileInteractor,
	                             UpdateUserAvatarInteractor updateUserAvatarInteractor,
	                             UpdateUserCoverInteractor updateUserCoverInteractor,
	                             FileClient fileClient) {

		mUploadFileInteractor = uploadFileInteractor;
		mUpdateUserProfileInteractor = updateUserProfileInteractor;
		mUpdateUserAvatarInteractor = updateUserAvatarInteractor;
		mUpdateUserCoverInteractor = updateUserCoverInteractor;
		mFileClient = fileClient;
	}

	@Override
	public void setView(UserEditView view) {
		mView = view;
	}

	@Override
	public void setUser(IUser user) {
		mView.showUsername(user.getUsername());
		mView.showDescription(user.getDescription());

		if (user.getProfilePicture() != null && Integer.valueOf(user.getProfilePicture()) != null) {
			Integer pictureId = Integer.valueOf(user.getProfilePicture());

			mFileClient.getFile(pictureId, new FileClient.Receiver() {

				@Override
				public void onGetFileUrl(String fileUrl) {
					mView.showAvatar(fileUrl);
				}
			});
		}

		if (user.getCoverPicture() != null && Integer.valueOf(user.getCoverPicture()) != null) {
			Integer coverId = Integer.valueOf(user.getCoverPicture());

			mFileClient.getFile(coverId, new FileClient.Receiver() {

				@Override
				public void onGetFileUrl(String fileUrl) {
					mView.showCover(fileUrl);
				}
			});
		}
	}

	@Override
	public void updateInfo(String username, String description) {
		mView.showLoading();

		if (mNewProfilePictureId != null) {
			mUpdateUserAvatarInteractor.setProfilePictureId(mNewProfilePictureId);
			mUpdateUserAvatarInteractor.execute(new UpdateUserInfoSubscriber(false));
		}
		if (mNewCoverPictureId != null) {
			mUpdateUserCoverInteractor.setCoverPictureId(mNewCoverPictureId);
			mUpdateUserCoverInteractor.execute(new UpdateUserInfoSubscriber(false));
		}
		
		mUpdateUserProfileInteractor.setUsername(username);
		mUpdateUserProfileInteractor.setDescription(description);
		mUpdateUserProfileInteractor.execute(new UpdateUserInfoSubscriber(true));
	}

	@Override
	public void postAvatar(File avatarFile) {
		mUploadFileInteractor.setFile(avatarFile, "image/jpeg");
		mUploadFileInteractor.execute(new FileUploadSubscriber(PICTURE_TYPE_AVATAR));
	}

	@Override
	public void postCover(File coverFile) {
		mUploadFileInteractor.setFile(coverFile, "image/jpeg");
		mUploadFileInteractor.execute(new FileUploadSubscriber((PICTURE_TYPE_COVER)));
	}

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	private final class FileUploadSubscriber extends DefaultSubscriber<IUploadResult> {

		private final int mType;

		FileUploadSubscriber(int type) {
			mType = type;
		}

		@Override
		public void onNext(IUploadResult uploadResult) {
			if (uploadResult.isSuccess()) {
				if (PICTURE_TYPE_AVATAR == mType) {
					mNewProfilePictureId = uploadResult.getId();
				} else if (PICTURE_TYPE_COVER == mType) {
					mNewCoverPictureId = uploadResult.getId();
				}
			} else {
				/** handle upload error */
			}
		}
	}

	private final class UpdateUserInfoSubscriber extends DefaultSubscriber<IUser> {

		private final boolean mShowEndMessage;

		UpdateUserInfoSubscriber(boolean showEndMessage) {
			mShowEndMessage = showEndMessage;
		}

		@Override
		public void onError(Throwable e) {
			mView.showFailureMessage(e.getLocalizedMessage());
		}

		@Override
		public void onNext(IUser user) {
			BusProvider.getInstance().post(new UserUpdateEvent(user));

			if (mShowEndMessage) {
				mView.hideLoading();
				mView.showSuccessMessage(R.string.user_edit_messages_user_info_update_successful);
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
