package com.onrushers.app.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import butterknife.ButterKnife
import com.onrushers.app.R

import com.onrushers.domain.business.model.INotification
import java.util.*

class NotificationsAdapter(context: Context) : BaseAdapter() {

    private var mNotificationsList: List<INotification> = ArrayList()

    private val mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater = LayoutInflater.from(context)
    }

    fun setNotifications(list: List<INotification>) {
        mNotificationsList = list.sortedByDescending { it.date }
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mNotificationsList.size
    }

    override fun getItem(i: Int): Any? {
        return mNotificationsList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val vh: NotificationViewHolder

        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.card_notification, parent, false)
            vh = NotificationViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as NotificationViewHolder
        }

        vh.configure(mNotificationsList[i])
        return view
    }
}
