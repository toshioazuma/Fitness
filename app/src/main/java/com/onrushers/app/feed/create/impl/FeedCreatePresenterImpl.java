package com.onrushers.app.feed.create.impl;

import android.content.Context;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.onrushers.app.R;
import com.onrushers.app.feed.create.FeedCreatePresenter;
import com.onrushers.app.feed.create.FeedCreateView;
import com.onrushers.common.utils.FileUtils;
import com.onrushers.domain.business.interactor.feed.CreateFeedInteractor;
import com.onrushers.domain.business.interactor.file.UploadFileInteractor;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUploadResult;
import com.onrushers.domain.business.model.IUploadedFile;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.common.DefaultSubscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FeedCreatePresenterImpl implements FeedCreatePresenter {

	private static final String TAG               = "FeedCreatePresenter";
	private static final String DEFAULT_MIME_TYPE = "image/jpeg";

	private FeedCreateView mView;
	private List<File>     mFiles;

	private final Context              mContext;
	private final CreateFeedInteractor mFeedInteractor;
	private final UploadFileInteractor mFileInteractor;

	@Inject
	public FeedCreatePresenterImpl(Context context, CreateFeedInteractor feedInteractor,
	                               UploadFileInteractor fileInteractor) {
		mContext = context;
		mFeedInteractor = feedInteractor;
		mFileInteractor = fileInteractor;
		mFiles = new ArrayList<>();
	}

	//region FeedCreatePresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setView(FeedCreateView view) {
		mView = view;
	}

	@Override
	public void setText(String text) {
		mFeedInteractor.setContent(text);
	}

	@Override
	public void setPlace(String place) {
		mFeedInteractor.setPlace(place);
	}

	@Override
	public void setTagUsers(List<IUser> users) {
		mFeedInteractor.setTagUsers(users);
	}

	@Override
	public void postPicture(File file) {
		mFiles.add(file);
	}

	@Override
	public void postFeed() {
		mView.showLoading();

		if (mFiles.size() > 0) {
			/**
			 * First, upload files to get its ID to attach to the next feed.
			 */
			final File firstFile = mFiles.remove(0);
			String mimeType = FileUtils.getMimeType(firstFile);

			if (mimeType == null || mimeType == "") {
				mimeType = DEFAULT_MIME_TYPE;
			}

			mFileInteractor.setFile(firstFile, mimeType);
			mFileInteractor.execute(new FileUploadSubscriber());

		} else {
			/**
			 * Post only the feed.
			 */
			mFeedInteractor.execute(new FeedCreateSubscriber());
		}
	}

	//----------------------------------------------------------------------------------------------

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	private class FileUploadSubscriber extends DefaultSubscriber<IUploadResult> {

		@Override
		public void onError(Throwable e) {
			e.printStackTrace();
			mView.showErrorMessage(e.getLocalizedMessage());
		}

		@Override
		public void onNext(IUploadResult iUploadResult) {

			if (iUploadResult.isSuccess()) {

				mFeedInteractor.addPhoto(iUploadResult.getId());

				if (mFiles.size() > 0) {
					/**
					 * Continue to upload files.
					 */
					final File firstFile = mFiles.remove(0);
					String mimeType = FileUtils.getMimeType(firstFile);

					if (mimeType == null || mimeType == "") {
						mimeType = DEFAULT_MIME_TYPE;
					}

					mFileInteractor.setFile(firstFile, mimeType);
					mFileInteractor.execute(new FileUploadSubscriber());

				} else {
					/**
					 * Post the feed.
					 */
					mFeedInteractor.execute(new FeedCreateSubscriber());
				}

			} else {
				mView.showErrorMessage(iUploadResult.getMessage());
			}
		}
	}

	private class FeedCreateSubscriber extends DefaultSubscriber<IFeed> {

		@Override
		public void onError(Throwable e) {
			mView.hideLoading();
			mView.showErrorMessage(mContext.getString(R.string.post_feed_messages_failure));
			e.printStackTrace();
		}

		@Override
		public void onNext(IFeed feed) {
			mView.hideLoading();
			mView.showSuccessMessage(mContext.getString(R.string.post_feed_messages_success));
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
