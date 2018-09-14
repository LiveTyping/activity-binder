package com.livetyping.vk

import android.app.Application
import com.livetyping.logincore.SocialNetwork
import com.vk.sdk.VKSdk


class VkNetwork : SocialNetwork {

    override fun init(app: Application) {
        VKSdk.initialize(app)
    }
}