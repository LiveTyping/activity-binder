package com.livetyping.google

import android.app.Activity
import android.content.Intent
import com.livetyping.logincore.SocialLoginError
import com.livetyping.logincore.SocialNetwork

class GoogleNetwork : SocialNetwork {

    override fun login(activity: Activity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, successBlock: (token: String) -> Unit, errorBlock: ((error: SocialLoginError) -> Unit)?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}