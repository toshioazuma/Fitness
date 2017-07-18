package com.onrushers.app.explore.tabs.ranking

import com.onrushers.domain.business.model.IUser

interface ExploreTabRankingView {

    fun showNoRanking()

    fun showUsers(users: List<IUser>, page: Int)
}
