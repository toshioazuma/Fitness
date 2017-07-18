package com.onrushers.common.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

	/**
	 * Create image file.
	 * To get path of the file, use "file:" and {@link File#getAbsolutePath()}
	 *
	 * @return {@link File} image file.
	 * @throws IOException
	 */
	public static final File createImageFile() throws IOException {
		/**
		 * Create an image file name
		 */
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String imageFileName = "jpg_" + timestamp;

		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, ".jpg", storageDir);

		return image;
	}

	/**
	 * Get the MIME type of file.
	 *
	 * @param file
	 * @return file MIME type.
	 */
	public static final String getMimeType(File file) {

		final String ext = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
		final String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);

		return mimeType;
	}

	/**
	 * Get real path from Uri resource.
	 *
	 * @param context
	 * @param contentUri
	 * @return real path of Uri resource.
	 */
	public static final String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

}
