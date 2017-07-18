package com.onrushers.app.notification

import com.onrushers.domain.business.model.INotification

interface NotificationsView {

    fun showNotifications(notifications: List<INotification>)
}
