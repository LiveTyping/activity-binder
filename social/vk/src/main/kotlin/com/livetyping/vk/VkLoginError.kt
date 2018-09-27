package com.livetyping.vk

import com.livetyping.logincore.SocialLoginError
import com.vk.sdk.api.VKError


class VkLoginError(private val error: VKError) : SocialLoginError {

    override fun error() = error.errorMessage
}