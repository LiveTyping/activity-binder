package com.livetyping.activitybinder

import android.app.Application
import com.livetyping.facebook.FacebookNetwork
import com.livetyping.logincore.SocialLoginBinder
import com.livetyping.vk.VkNetwork

class BinderExampleApplication : Application() {

    val socialLoginBinder: SocialLoginBinder by lazy {
        SocialLoginBinder()
    }

    override fun onCreate() {
        super.onCreate()
        socialLoginBinder.initializeSocialNetworks(this,
                listOf(
                        VkNetwork(),
                        FacebookNetwork()
                ))
    }
}
