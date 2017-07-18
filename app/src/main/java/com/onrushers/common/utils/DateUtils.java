package com.onrushers.common.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static final int getAge(Date dateOfBirth) {

		final Calendar dobCal = Calendar.getInstance();
		dobCal.setTime(dateOfBirth);

		final Calendar nowCal = Calendar.getInstance();

		int age = nowCal.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR);

		boolean isMonthGreater = dobCal.get(Calendar.MONTH) >= nowCal.get(Calendar.MONTH);

		boolean isMonthSameButDayGreater = dobCal.get(Calendar.MONTH) == nowCal.get(Calendar.MONTH)
				&& dobCal.get(Calendar.DAY_OF_MONTH) > nowCal.get(Calendar.DAY_OF_MONTH);

		if (isMonthGreater || isMonthSameButDayGreater) {
			age = age - 1;
		}

		age = Math.max(age, 0);
		return age;
	}
}
