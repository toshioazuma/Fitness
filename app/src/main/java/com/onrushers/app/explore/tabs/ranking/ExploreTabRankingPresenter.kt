package com.onrushers.app.explore.tabs.ranking

interface ExploreTabRankingPresenter {

    fun setView(view: ExploreTabRankingView)

    fun onViewCreated()

    fun onDestroyView()

    fun fetchRankUsersAtPage(page: Int)
}
