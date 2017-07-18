package com.onrushers.app.user.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.onrushers.app.R
import com.onrushers.domain.business.model.IUser
import java.util.*

class UserListAdapter(context: Context) : BaseAdapter() {

    private var mUsers: ArrayList<IUser> = ArrayList()

    private val mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater = LayoutInflater.from(context)
    }

    fun appendUsers(users: List<IUser>, page: Int) {
        if (page == 1) {
            mUsers.clear()
        }
        mUsers.addAll(users)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mUsers.size
    }

    override fun getItem(i: Int): Any? {
        return mUsers[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val vh: UserListItemViewHolder

        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.card_user_list_row, parent, false)
            vh = UserListItemViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as UserListItemViewHolder
        }

        vh.setUser(mUsers[i])
        return view
    }
}
