package com.onrushers.app.picture.picker;

import android.content.Context;

import com.onrushers.app.R;
import com.onrushers.common.utils.ToastUtils;

import java.io.File;

public class PicturePickerAgentCallbacksImpl implements PicturePickerAgent.Callbacks {

	private final Context mContext;

	public PicturePickerAgentCallbacksImpl(Context context) {
		mContext = context;
	}

	@Override public void onPicturePickerCannotUseCamera() {
		ToastUtils.showText(mContext, R.string.picker_picture_error_cannot_use_camera);
	}

	@Override public void onPicturePickerCameraDenied() {
		ToastUtils.showText(mContext, R.string.picker_picture_error_camera_permission_denied);
	}

	@Override public void onPicturePickerCannotUseLibrary() {
		ToastUtils.showText(mContext, R.string.picker_picture_error_cannot_use_library);
	}

	@Override public void onPicturePickerCreateFileDenied() {
		ToastUtils.showText(mContext, R.string.picker_picture_error_write_permission_denied);
	}

	@Override public void onPicturePickerCannotCreateFile() {
		ToastUtils.showText(mContext, R.string.picker_picture_error_file_creation_failed);
	}

	@Override public void onPicturePickerImportFailed() {
		ToastUtils.showText(mContext, R.string.picker_picture_error_picture_import_failed);
	}

	@Override public void onPicturePickerGotUnknownError() {
		ToastUtils.showText(mContext, R.string.picker_picture_error_unknown_error);
	}

	@Override public void onPicturePickerDidCropFile(File file, int tag) {

	}
}
