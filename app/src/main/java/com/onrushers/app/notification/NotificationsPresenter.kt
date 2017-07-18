package com.onrushers.app.notification

interface NotificationsPresenter {

    fun setView(view: NotificationsView)

    fun onViewCreated()

    fun onDestroy()
}
