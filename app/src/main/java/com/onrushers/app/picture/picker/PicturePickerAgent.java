package com.onrushers.app.picture.picker;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.onrushers.app.R;
import com.onrushers.app.picture.cropper.PictureCropperActivity;
import com.onrushers.app.picture.cropper.PictureCropperMode;
import com.onrushers.common.utils.FileUtils;

import java.io.File;
import java.io.IOException;

public class PicturePickerAgent implements PicturePickerBottomSheetDialogFragment.ItemListener {

	public static final String TAG = "PicturePickerAgent";

	private static final int REQUEST_CODE_CAMERA  = 1;
	private static final int REQUEST_CODE_LIBRARY = 2;
	private static final int REQUEST_CODE_CROPPER = 100;

	private static final int PERMISSION_REQUEST_CODE_USE_CAMERA = 200;
	private static final int PERMISSION_REQUEST_CODE_WRITE_FILE = 201;

	private static PicturePickerAgent mAgent;

	private Fragment mFragment;
	private String   mCurrentNewPicturePath;

	private PicturePickerBottomSheetDialogFragment mPickerSheetFragment;

	private Callbacks mCallbacks = null;
	private int       mTag       = -1;
	private String    mCropMode  = PictureCropperMode.SQUARE;


	public static PicturePickerAgent getAgent(Fragment fragment) {
		if (mAgent == null) {
			mAgent = new PicturePickerAgent(fragment);
		} else {
			mAgent.mFragment = fragment;
		}
		return mAgent;
	}

	public static PicturePickerAgent getCurrentAgent() {
		if (mAgent == null || mAgent.mFragment == null) {
			return null;
		}
		return mAgent;
	}

	private PicturePickerAgent(Fragment fragment) {
		mFragment = fragment;
	}

	//region Public
	//----------------------------------------------------------------------------------------------

	public PicturePickerAgent setCallbacksListener(Callbacks listener) {
		mCallbacks = listener;
		return this;
	}

	public void presentBottomSheetDialog() {

		if (mPickerSheetFragment == null) {
			mPickerSheetFragment = new PicturePickerBottomSheetDialogFragment();
		}
		mPickerSheetFragment.setListener(this);

		/**
		 * Bottom sheet showing 2 options:
		 * - take a new picture (from camera)
		 * - take an existing picture (from gallery or other apps)
		 */
		mPickerSheetFragment.show(mFragment.getChildFragmentManager(),
			PicturePickerBottomSheetDialogFragment.TAG);
	}

	public PicturePickerAgent setTag(int tag) {
		mTag = tag;
		return this;
	}

	public PicturePickerAgent setCropMode(String mode) {
		mCropMode = mode;
		return this;
	}

	public static boolean isPicturePickerSupported(int requestCode) {

		if (REQUEST_CODE_CAMERA == requestCode ||
			REQUEST_CODE_LIBRARY == requestCode ||
			REQUEST_CODE_CROPPER == requestCode) {
			return true;
		}
		return false;
	}

	public static boolean canHandlePermissionsResult(int requestCode) {
		if (PERMISSION_REQUEST_CODE_USE_CAMERA == requestCode ||
			PERMISSION_REQUEST_CODE_WRITE_FILE == requestCode) {
			return true;
		}
		return false;
	}

	//endregion
	//----------------------------------------------------------------------------------------------

	//region Handle fragment callbacks
	//----------------------------------------------------------------------------------------------

	public void handleActivityResult(int requestCode, int resultCode, Intent data) {

		final Context context = mFragment.getContext();

		if (REQUEST_CODE_CAMERA == requestCode && resultCode == Activity.RESULT_OK) {

			Intent cropperIntent = new Intent(context, PictureCropperActivity.class);

			cropperIntent.putExtra(
				PictureCropperActivity.EXTRA_PICTURE_PATH, mCurrentNewPicturePath);
			cropperIntent.putExtra(
				PictureCropperActivity.EXTRA_CROP_TYPE, mCropMode);

			mFragment.startActivityForResult(cropperIntent, REQUEST_CODE_CROPPER);

		} else if (REQUEST_CODE_LIBRARY == requestCode && resultCode == Activity.RESULT_OK) {

			Uri selectedUri = data.getData();

			if (selectedUri == null && mCallbacks != null) {
				mCallbacks.onPicturePickerImportFailed();
				return;
			}

			mCurrentNewPicturePath = selectedUri.toString();

			/**
			 * Start crop with external picture.
			 */
			Intent cropperIntent = new Intent(context, PictureCropperActivity.class);

			cropperIntent.putExtra(
				PictureCropperActivity.EXTRA_PICTURE_PATH, mCurrentNewPicturePath);
			cropperIntent.putExtra(
				PictureCropperActivity.EXTRA_CROP_TYPE, mCropMode);

			mFragment.startActivityForResult(cropperIntent, REQUEST_CODE_CROPPER);

		} else if (REQUEST_CODE_CROPPER == requestCode && resultCode == Activity.RESULT_OK) {
			/**
			 * Get cropped picture from intent extras.
			 */
			String croppedPath = data.getStringExtra(PictureCropperActivity.EXTRA_CROPPED_PATH);

			if (croppedPath != null && mCallbacks != null) {
				File croppedFile = new File(croppedPath);
				mCallbacks.onPicturePickerDidCropFile(croppedFile, mTag);
			}
		}
	}

	public void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		if (PERMISSION_REQUEST_CODE_USE_CAMERA == requestCode) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				/** Restart the 'take a picture from camera' flow */
				onPickerNewPictureClick(null);
			} else {
				Log.e(TAG, "error: permission to use the camera is not satisfied !");
				if (mCallbacks != null) {
					mCallbacks.onPicturePickerCameraDenied();
				}
			}
		} else if (PERMISSION_REQUEST_CODE_WRITE_FILE == requestCode) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				/** Restart the 'take a picture from camera' flow */
				onPickerNewPictureClick(null);
			} else {
				Log.e(TAG, "error: permission to write on external storage is not satisfied !");
				if (mCallbacks != null) {
					mCallbacks.onPicturePickerCreateFileDenied();
				}
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region PicturePickerBottomSheetDialogFragment.ItemListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onPickerDismissClick(Dialog dialog) {
		dialog.dismiss();
	}

	@Override
	public void onPickerNewPictureClick(Dialog dialog) {
		/**
		 * First checks if device can take picture.
		 */
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (intent.resolveActivity(mFragment.getActivity().getPackageManager()) == null) {
			Log.e(TAG, "error: cannot resolve activity [" + MediaStore.ACTION_IMAGE_CAPTURE + "]");
			if (mCallbacks != null) {
				mCallbacks.onPicturePickerCannotUseCamera();
			}
			return;
		}

		/**
		 * Checks also if the app is allowed to write into the user storage.
		 */
		int storagePermission = ContextCompat.checkSelfPermission(mFragment.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (storagePermission != PackageManager.PERMISSION_GRANTED) {
			/**
			// https://developer.android.com/training/permissions/requesting.html
			if (ActivityCompat.shouldShowRequestPermissionRationale(mFragment.getActivity(), Manifest.permission.CAMERA)) {

			}
			*/

			dialog.dismiss();

			ActivityCompat.requestPermissions(mFragment.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE_WRITE_FILE);
			return;
		}

		File photoFile;
		try {
			photoFile = FileUtils.createImageFile();
			mCurrentNewPicturePath = "file:" + photoFile.getAbsolutePath();
		} catch (IOException ex) {
			Log.e(TAG, "error: while creating the File");
			ex.printStackTrace();
			if (mCallbacks != null) {
				mCallbacks.onPicturePickerCannotCreateFile();
			}
			return;
		}

		/**
		 * Continue only if the File was successfully created
		 */
		if (photoFile != null) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
			mFragment.startActivityForResult(intent, REQUEST_CODE_CAMERA);
		} else {
			Log.e(TAG, "error: file was not created");
			if (mCallbacks != null) {
				mCallbacks.onPicturePickerGotUnknownError();
			}
		}

		if (dialog != null) {
			dialog.dismiss();
		}
	}

	@Override
	public void onPickerFromLibraryClick(Dialog dialog) {
		Log.d(TAG, "onPickerFromLibraryClick");

		/**
		 * Import an existing picture from library.
		 */
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/jpg");

		if (intent.resolveActivity(mFragment.getActivity().getPackageManager()) == null) {
			Log.e(TAG, "error: cannot resolve activity [" + Intent.ACTION_PICK + "]");
			if (mCallbacks != null) {
				mCallbacks.onPicturePickerCannotUseLibrary();
			}
			return;
		}

		final Intent intentChooser = Intent
			.createChooser(intent, mFragment.getContext().getString(R.string.picker_picture_library_select_picture));
		mFragment.startActivityForResult(intentChooser, REQUEST_CODE_LIBRARY);

		dialog.dismiss();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	public interface Callbacks {

		void onPicturePickerCannotUseCamera();

		void onPicturePickerCameraDenied();

		void onPicturePickerCannotUseLibrary();

		void onPicturePickerCreateFileDenied();

		void onPicturePickerCannotCreateFile();

		void onPicturePickerImportFailed();

		void onPicturePickerGotUnknownError();

		void onPicturePickerDidCropFile(File file, int tag);
	}
}
