package com.onrushers.app.notification

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.onrushers.app.R
import com.onrushers.app.common.Font
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan

object NotificationViewPresenter {

    fun getCommentSpan(context: Context, username: String): SpannableString {

        val text = context.getString(R.string.notifications_formats_comment).format(username)
        val span = SpannableString(text)

        val start: Int = text.indexOf(username)
        val end: Int = start + username.length

        span.setSpan(CalligraphyTypefaceSpan(monserratBoldTypeface(context)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return span
    }

    fun getRushSpan(context: Context, username: String, rushText: String): SpannableString {

        val blueColor = ContextCompat.getColor(context, R.color.blueSecondary)

        val text = context.getString(R.string.notifications_formats_rush).format(username, rushText.toUpperCase())
        val span = SpannableString(text)

        var start: Int = text.indexOf(username)
        var end: Int = start + username.length

        span.setSpan(CalligraphyTypefaceSpan(monserratBoldTypeface(context)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        start = text.lastIndexOf(rushText.toUpperCase())
        end = start + rushText.length

        span.setSpan(CalligraphyTypefaceSpan(monserratBoldTypeface(context)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(ForegroundColorSpan(blueColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return span
    }

    fun getFanSpan(context: Context, username: String, fanText: String): SpannableString {

        val greenColor = ContextCompat.getColor(context, R.color.green)

        val text = context.getString(R.string.notifications_formats_fan).format(username, fanText.toUpperCase())
        val span = SpannableString(text)

        var start: Int = text.indexOf(username)
        var end: Int = start + username.length

        span.setSpan(CalligraphyTypefaceSpan(monserratBoldTypeface(context)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        start = text.lastIndexOf(fanText.toUpperCase())
        end = start + fanText.length

        span.setSpan(CalligraphyTypefaceSpan(monserratBoldTypeface(context)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(ForegroundColorSpan(greenColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return span
    }

    fun getEventSpan(context: Context, username: String, eventName: String): SpannableString {

        val blueColor = ContextCompat.getColor(context, R.color.blueSecondary)

        val text = context.getString(R.string.notifications_formats_event).format(username, eventName)
        val span = SpannableString(text)

        var start: Int = text.indexOf(username)
        var end: Int = start + username.length

        span.setSpan(CalligraphyTypefaceSpan(monserratBoldTypeface(context)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        start = text.lastIndexOf(eventName)
        end = start + eventName.length

        span.setSpan(ForegroundColorSpan(blueColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return span
    }

    private fun monserratBoldTypeface(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, Font.MONTSERRAT_BOLD)
    }
}
