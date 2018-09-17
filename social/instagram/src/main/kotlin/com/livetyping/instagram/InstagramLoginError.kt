package com.livetyping.instagram

import com.livetyping.logincore.SocialLoginError


class InstagramLoginError(private val exception: Exception) : SocialLoginError {

    override fun error() = exception.toString()

}