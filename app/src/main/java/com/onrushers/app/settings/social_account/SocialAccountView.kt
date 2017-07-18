package com.onrushers.app.settings.social_account

import android.support.annotation.Nullable
import android.text.SpannableString

interface SocialAccountView {

    fun switchOffFacebookProfile()

    fun switchOnFacebookProfile(@Nullable facebookUsername: SpannableString)

    fun showFacebookUserName(@Nullable userNameSpan: SpannableString)

    fun performFetchFacebookUserId()
}
