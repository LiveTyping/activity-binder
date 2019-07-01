package com.livetyping.vk

import com.livetyping.logincore.SocialLoginResult
import com.vk.sdk.VKAccessToken


class VkLoginResult(val accessToken: String, val email: String?) : SocialLoginResult {

}

internal fun VKAccessToken.toVkLoginResult(): VkLoginResult = VkLoginResult(accessToken, email)
