package com.onrushers.common.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.lang.ref.SoftReference;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment
	implements DatePickerDialog.OnDateSetListener {

	public static final String TAG = "DatePicker";

	private SoftReference<DatePickerFragment.OnDateSetListener>       mListener;
	private SoftReference<DatePickerFragment.OnDateObjectSetListener> mObjectListener;

	private int mSelectedYear  = -1;
	private int mSelectedMonth = -1;
	private int mSelectedDay   = -1;

	public void setOnDateSetListener(DatePickerFragment.OnDateSetListener listener) {
		if (listener != null) {
			mListener = new SoftReference<>(listener);
		} else {
			mListener = null;
		}
	}

	public void setOnDateObjectSetListener(DatePickerFragment.OnDateObjectSetListener listener) {
		if (listener != null) {
			mObjectListener = new SoftReference<>(listener);
		} else {
			mObjectListener = null;
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		/**
		 * Use the current date as the default date in the picker
		 */
		final Calendar c = Calendar.getInstance();

		int year = (mSelectedYear == -1) ? c.get(Calendar.YEAR) : mSelectedYear;
		int month = (mSelectedMonth == -1) ? c.get(Calendar.MONTH) : mSelectedMonth;
		int day = (mSelectedDay == -1) ? c.get(Calendar.DAY_OF_MONTH) : mSelectedDay;

		/**
		 * Create a new instance of DatePickerDialog and return it
		 */
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}


	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		mSelectedYear = year;
		mSelectedMonth = monthOfYear;
		mSelectedDay = dayOfMonth;

		if (mListener != null) {
			final Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, monthOfYear);

			String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			mListener.get().onDateSet(view, year, monthOfYear, month, dayOfMonth);

		} else if (mObjectListener != null) {

			final Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			mObjectListener.get().onDateSet(view, cal.getTime());
		}
	}

	public interface OnDateSetListener {
		void onDateSet(DatePicker view, int year, int monthOfYear, String monthName, int day);
	}

	public interface OnDateObjectSetListener {
		void onDateSet(DatePicker view, Date date);
	}
}
