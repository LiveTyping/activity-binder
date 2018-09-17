package com.livetyping.activitybinder

import android.app.Application
import com.livetyping.facebook.FacebookInitializer
import com.livetyping.instagram.InstagramInitializer
import com.livetyping.logincore.SocialLoginBinder
import com.livetyping.vk.VkInitializer

class BinderExampleApplication : Application() {

    val socialLoginBinder: SocialLoginBinder by lazy {
        SocialLoginBinder()
    }

    override fun onCreate() {
        super.onCreate()
        socialLoginBinder.initializeNetworks(this,
                listOf(
                        VkInitializer(),
                        FacebookInitializer(),
                        InstagramInitializer()
                ))
    }
}
