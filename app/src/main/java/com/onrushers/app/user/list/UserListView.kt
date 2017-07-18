package com.onrushers.app.user.list

import com.onrushers.domain.business.model.IUser

interface UserListView {

    fun setTitle(titleResId: Int)

    fun setConfiguration(configuration: UserListConfiguration)

    fun showLoading()

    fun hideLoading()

    fun showUsers(users: List<IUser>, page: Int)
}
