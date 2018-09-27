package com.livetyping.facebook

import android.app.Application
import com.facebook.appevents.AppEventsLogger
import com.livetyping.logincore.SocialInitializer


class FacebookInitializer : SocialInitializer {

    override fun init(app: Application) {
        AppEventsLogger.activateApp(app)
    }

}