package com.onrushers.common.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.ViewParent;

import com.onrushers.app.R;

/**
 * Purpose of ORTextInputEditText:
 * <p/>
 * Enforces connectivity between edit text and its layout
 * {@link android.support.design.widget.TextInputLayout}
 * <p/>
 * If its layout is present, it's will handle error message directly,
 * otherwise error will be shown inside this edit text itself.
 * <p/>
 * TextInputLayout of ORTextInputEditText can be link with the specific attribute app:layoutParent
 * (accept a resource ID).
 * <p/>
 * If no error, automatically disable error and dedicated used space.
 */
public class ORTextInputEditText extends TextInputEditText {

	private final static String TAG = "ORTextInputEditText";

	private TextInputLayout mInputLayout;

	private int mDefaultTextColor;
	private int mErrorTextColor;

	public ORTextInputEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupAttributes(attrs);
		setupDefaults();
	}

	private void setupAttributes(AttributeSet attrs) {
		/**
		 * Obtain typed array of attributes
		 */
		TypedArray a = getContext().getTheme()
				.obtainStyledAttributes(attrs, R.styleable.ORTextInputEditText, 0, 0);
		try {
			/**
			 * Extract custom attributes into member variables
			 */
			mErrorTextColor = a.getColor(R.styleable.ORTextInputEditText_errorTextColor, Color.RED);
		} finally {
			/**
			 * Recycle typed array
			 */
			a.recycle();
		}
	}

	private void setupDefaults() {
		mDefaultTextColor = getCurrentTextColor();
	}

	public void setInputLayout(TextInputLayout inputLayout) {
		mInputLayout = inputLayout;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		final ViewParent viewParent = getParent();

		if (viewParent != null && viewParent instanceof TextInputLayout) {
			mInputLayout = (TextInputLayout) viewParent;
		}
	}

	@Override
	public void setError(CharSequence error) {

		if (error == null) {
			/**
			 * Clear error used height.
			 */
			if (mInputLayout != null) {
				mInputLayout.setErrorEnabled(false);
			}

			setTextColor(mDefaultTextColor);
		}
		else if (mInputLayout != null) {

			mInputLayout.setErrorEnabled(true);

			/**
			 * Fix: to force update text, append or trim space first, then affects error message.
			 */
			if (mInputLayout.getError() != null) {
				String previousError = mInputLayout.getError().toString();

				if (error.equals(previousError)) {
					if (previousError.endsWith(" ")) {
						error = previousError.trim();
					} else {
						error = previousError.concat(" ");
					}
				}
			}

			mInputLayout.setError(error);
		}
		else {
			super.setError(error);
		}
	}
}
