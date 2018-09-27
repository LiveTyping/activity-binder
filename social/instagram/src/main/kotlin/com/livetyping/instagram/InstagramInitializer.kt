package com.livetyping.instagram

import android.app.Application
import com.livetyping.logincore.SocialInitializer
import com.nikola.jakshic.instagramauth.InstagramAuth


class InstagramInitializer : SocialInitializer {

    override fun init(app: Application) {
        InstagramAuth.initialize(app)
    }
}