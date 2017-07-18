package com.onrushers.app.notification

import android.content.Context
import android.text.TextUtils
import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.mikhaellopez.circularimageview.CircularImageView
import com.onrushers.app.R
import com.onrushers.app.file.Downloader
import com.onrushers.app.file.FileClient
import com.onrushers.domain.business.model.INotification
import com.onrushers.domain.business.type.NotificationType

class NotificationViewHolder {

    var mContextImageView: ImageView? = null
    var mFromUserImageView: CircularImageView? = null
    var mBadgeView: View? = null
    var mTimeAgoTextView: TextView? = null
    var mTextView: TextView? = null

    val context: Context

    constructor(view: View) {
        mContextImageView = view.findViewById(R.id.notification_context_imageview) as ImageView?
        mFromUserImageView = view.findViewById(R.id.notification_from_user_imageview) as? CircularImageView
        mBadgeView = view.findViewById(R.id.notification_type_badge_imageview)
        mTimeAgoTextView = view.findViewById(R.id.notification_time_ago_textview) as TextView?
        mTextView = view.findViewById(R.id.notification_text_textview) as TextView?
        context = view.context
    }

    fun configure(notification: INotification) {

        if (notification.date != null) {
            val timeAgo = DateUtils.getRelativeDateTimeString(context, notification.date.time, 0, DateUtils.WEEK_IN_MILLIS, 0)
            mTimeAgoTextView?.text = (timeAgo as String).toLowerCase()
        } else {
            mTimeAgoTextView?.setText(R.string.empty)
        }

        when (notification.notificationType) {
            NotificationType.Comment, NotificationType.Rushed -> {
                mBadgeView?.setBackgroundResource(R.drawable.sh_badge_blue)

                if (notification.notificationType == NotificationType.Comment) {
                    mTextView?.text = NotificationViewPresenter.getCommentSpan(context, notification.fromUser.username)
                } else {
                    mTextView?.text = NotificationViewPresenter.getRushSpan(context, notification.fromUser.username, notification.notificationType.name)
                }

                if (notification.postPictureId != null) {
                    val fileUrl: String = Downloader.instance.resourceUrl(notification.postPictureId)
                    Glide.with(context).load(fileUrl).asBitmap().centerCrop().into(mContextImageView)
                    mContextImageView?.visibility = View.VISIBLE
                } else {
                    mContextImageView?.visibility = View.INVISIBLE
                }
            }
            NotificationType.Fan -> {
                mBadgeView?.setBackgroundResource(R.drawable.sh_badge_green)
                mTextView?.text = NotificationViewPresenter.getFanSpan(context, notification.fromUser.username, notification.notificationType.name)
                mContextImageView?.setImageResource(R.drawable.ic_notification_type_fan)
                mContextImageView?.visibility = View.VISIBLE
            }
            NotificationType.Event -> {
                mBadgeView?.setBackgroundResource(R.drawable.sh_badge_black)
                mTextView?.text = NotificationViewPresenter.getEventSpan(context, notification.fromUser.username, notification.notificationType.name)
                mContextImageView?.setImageResource(R.drawable.ic_notification_type_event)
                mContextImageView?.visibility = View.VISIBLE
            }
            else -> {
            }
        }

        val fromUser = notification.fromUser
        if (fromUser != null && !TextUtils.isEmpty(fromUser.profilePicture)) {
            val fileUrl: String = Downloader.instance.resourceUrl(fromUser.profilePicture)
            Glide.with(context).load(fileUrl).asBitmap().centerCrop().into(mFromUserImageView)
        } else {
            mFromUserImageView?.setImageResource(R.drawable.ic_user_avatar_default)
        }
    }
}

