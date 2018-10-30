package com.livetyping.google

import android.app.Application
import com.livetyping.logincore.SocialInitializer

class GoogleInitializer : SocialInitializer {

    override fun init(app: Application) {
        // pass (google does not require static initialization)
    }
}