package com.livetyping.facebook

import android.app.Activity
import android.content.Intent
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.livetyping.logincore.SocialLoginError
import com.livetyping.logincore.SocialNetwork


class FacebookNetwork : SocialNetwork<FacebookLoginResult> {

    private val callbackManager = CallbackManager.Factory.create()

    override fun login(activity: Activity) {
        LoginManager.getInstance().logInWithReadPermissions(activity,
                arrayListOf("public_profile", "email"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?,
                                  successBlock: (result: FacebookLoginResult) -> Unit,
                                  errorBlock: ((error: SocialLoginError) -> Unit)?) {
        LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                    override fun onSuccess(result: LoginResult) {
                        successBlock.invoke(result.toFacebookLoginResult())
                    }

                    override fun onCancel() {
                        LoginManager.getInstance().unregisterCallback(callbackManager)
                    }

                    override fun onError(error: FacebookException) {
                        errorBlock?.invoke(FacebookLoginError(error))
                    }
                })
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
