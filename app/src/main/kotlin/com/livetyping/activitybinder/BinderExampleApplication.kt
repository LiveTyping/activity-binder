package com.livetyping.activitybinder

import android.app.Application
import com.livetyping.facebook.FacebookInitializer
import com.livetyping.images.ImagesBinder
import com.livetyping.instagram.InstagramInitializer
import com.livetyping.logincore.SocialLoginBinder
import com.livetyping.permission.PermissionBinder
import com.livetyping.vk.VkInitializer

class BinderExampleApplication : Application() {

    val socialLoginBinder: SocialLoginBinder by lazy {
        SocialLoginBinder()
    }

    val permissionBinder: PermissionBinder by lazy {
        PermissionBinder()
    }

    val imagesBinder: ImagesBinder by lazy {
        ImagesBinder(applicationContext.packageName + ".provider", R.xml.file_path)
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
