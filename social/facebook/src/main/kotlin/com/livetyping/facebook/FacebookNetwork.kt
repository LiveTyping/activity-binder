package com.livetyping.facebook

import android.app.Application
import com.facebook.appevents.AppEventsLogger
import com.livetyping.logincore.SocialNetwork


class FacebookNetwork : SocialNetwork {

    override fun init(app: Application) {
        AppEventsLogger.activateApp(app)
    }

}