package com.livetyping.vk

import android.app.Application
import com.livetyping.logincore.SocialInitializer
import com.vk.sdk.VKSdk


class VkInitializer : SocialInitializer {

    override fun init(app: Application) {
        VKSdk.initialize(app)
    }
}