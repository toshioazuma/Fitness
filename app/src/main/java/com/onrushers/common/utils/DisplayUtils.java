package com.onrushers.common.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtils {

	/**
	 * This method converts dp unit to equivalent pixels, depending on device density.
	 *
	 * @param dp
	 * 		A value in dp (density independent pixels) unit. Which we need to convert into
	 * 		pixels
	 *
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	public static float convertDpToPixel(Context appContext, float dp) {
		Resources resources = appContext.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return (float) Math.ceil(dp * (metrics.densityDpi / 160f));
	}

	/**
	 * This method converts device specific pixels to density independent pixels.
	 *
	 * @param px
	 * 		A value in px (pixels) unit. Which we need to convert into db
	 *
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(Context appContext, float px) {
		Resources resources = appContext.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	/**
	 * Returns the current screen dimensions in device independent pixels (DIP) as a {@link Point} object where
	 * {@link Point#x} is the screen width and {@link Point#y} is the screen height.
	 *
	 * @param context Context instance
	 *
	 * @return The current screen dimensions in DIP.
	 */
	public static Point getScreenSize(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Configuration configuration = context.getResources().getConfiguration();
			return new Point(configuration.screenWidthDp, configuration.screenHeightDp);

		} else {
			// APIs prior to v13 gave the screen dimensions in pixels. We convert them to DIPs before returning them.
			WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics displayMetrics = new DisplayMetrics();
			windowManager.getDefaultDisplay().getMetrics(displayMetrics);

			int screenWidthInDIP = (int) convertPixelsToDp(context, displayMetrics.widthPixels);
			int screenHeightInDIP = (int) convertPixelsToDp(context, displayMetrics.heightPixels);
			return new Point(screenWidthInDIP, screenHeightInDIP);
		}
	}
}
