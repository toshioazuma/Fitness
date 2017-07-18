package com.onrushers.app.settings.social_account

import android.content.Context
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

interface SocialAccountPresenter {

    //region Object life cycle
    //----------------------------------------------------------------------------------------------

    fun setView(view: SocialAccountView)

    fun onViewCreated()

    fun destroyView()

    //----------------------------------------------------------------------------------------------
    //endregion

    //region Event handling
    //----------------------------------------------------------------------------------------------

    fun linkFacebookProfile(facebookId: String)

    fun unlinkFacebookProfile()

    fun presentFacebookUserName(@NotNull context: Context, @Nullable username: String)

    //----------------------------------------------------------------------------------------------
    //endregion
}
