package com.onrushers.app.explore.tabs.ranking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.onrushers.app.R
import com.onrushers.app.explore.tabs.ranking.views.ExploreRankViewHolder
import com.onrushers.app.user.OnUserDetailListener
import com.onrushers.domain.business.model.IUser
import java.util.*

class ExploreTabRankingAdapter(context: Context) : BaseAdapter() {

    private var mUsers: ArrayList<IUser> = ArrayList()

    private val mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater = LayoutInflater.from(context)
    }

    fun addUsers(users: List<IUser>) {
        mUsers.addAll(users)
        mUsers.sortBy { it.rank }

        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mUsers.count()
    }

    override fun getItem(i: Int): Any? {
        return mUsers[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val vh: ExploreRankViewHolder

        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.card_rank, parent, false)
            vh = ExploreRankViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ExploreRankViewHolder
        }

        vh.setUser(mUsers[i])
        return view
    }
}
