package com.onrushers.common.utils;

import android.content.Context;
import android.widget.Toast;

public final class ToastUtils {

	public static final void showText(Context ctx, int resId) {
		Toast.makeText(ctx, resId, Toast.LENGTH_SHORT).show();
	}

	public static final void showText(Context ctx, String text) {
		Toast.makeText(ctx, text, Toast.LENGTH_LONG).show();
	}

}
