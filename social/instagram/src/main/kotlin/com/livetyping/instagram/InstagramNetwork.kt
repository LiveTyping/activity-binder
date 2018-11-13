package com.livetyping.instagram

import android.app.Activity
import android.content.Intent
import com.livetyping.logincore.SocialLoginError
import com.livetyping.logincore.SocialNetwork
import com.nikola.jakshic.instagramauth.AuthManager
import com.nikola.jakshic.instagramauth.InstagramAuthException


class InstagramNetwork : SocialNetwork<InstagramLoginResult> {

    private var successBlock: ((token: InstagramLoginResult) -> Unit)? = null
    private var errorBlock: ((error: SocialLoginError) -> Unit)? = null

    override fun login(activity: Activity) {
        AuthManager.getInstance().login(activity, object : AuthManager.LoginCallback {
            override fun onError(e: InstagramAuthException) {
                errorBlock?.apply { invoke(InstagramLoginError(e)) }
            }

            override fun onSuccess() {
                successBlock?.apply {
                    invoke(InstagramLoginResult(AuthManager.getInstance().getToken().orEmpty()))
                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?,
                                  successBlock: (token: InstagramLoginResult) -> Unit,
                                  errorBlock: ((error: SocialLoginError) -> Unit)?) {
        this.successBlock = successBlock
        this.errorBlock = errorBlock
        AuthManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

}
