package com.livetyping.google

import com.google.android.gms.common.api.ApiException
import com.livetyping.logincore.SocialLoginError

class GoogleLoginError(private val apiException: ApiException) : SocialLoginError {

    override fun error() = apiException.message.orEmpty()
}