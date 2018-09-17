package com.livetyping.facebook

import com.facebook.FacebookException
import com.livetyping.logincore.SocialLoginError


class FacebookLoginError(private val exception: FacebookException) : SocialLoginError {

    override fun error() = exception.message.orEmpty()
}