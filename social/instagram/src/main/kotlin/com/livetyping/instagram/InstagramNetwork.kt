package com.livetyping.instagram

import android.app.Application
import com.livetyping.logincore.SocialNetwork
import com.nikola.jakshic.instagramauth.InstagramAuth


class InstagramNetwork : SocialNetwork {

    override fun init(app: Application) {
        InstagramAuth.initialize(app)
    }
}