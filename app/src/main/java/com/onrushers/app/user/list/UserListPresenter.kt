package com.onrushers.app.user.list

interface UserListPresenter {

    fun setView(view: UserListView)

    fun fetchContextListAtPage(page: Int)

    fun fetchNextContextListPage()
}
