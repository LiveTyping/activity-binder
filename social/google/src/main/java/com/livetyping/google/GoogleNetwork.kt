package com.livetyping.google

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.livetyping.logincore.SocialLoginError
import com.livetyping.logincore.SocialNetwork
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

class GoogleNetwork(serverClientId: String) : SocialNetwork {
    companion object {
        private const val GOOGLE_SIGN_IN_ACTIVITY_REQUEST_CODE = 231
    }

    private var context: Context? = null

    private val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(serverClientId)
            .requestId()
            .requestProfile()
            .requestEmail()
            .build()

    override fun login(activity: Activity) {
        context = activity
        val signInClient = GoogleSignIn.getClient(activity, signInOptions)
        activity.startActivityForResult(signInClient.signInIntent, GOOGLE_SIGN_IN_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?,
                                  successBlock: (token: String) -> Unit,
                                  errorBlock: ((error: SocialLoginError) -> Unit)?) {
        if (requestCode == GOOGLE_SIGN_IN_ACTIVITY_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val scopes = "oauth2:profile email"
                launch(CommonPool) {
                    val accessToken = GoogleAuthUtil.getToken(this@GoogleNetwork.context, account!!.account, scopes)
                    successBlock(accessToken)
                }
            } catch (e: ApiException) {
                errorBlock?.invoke(GoogleLoginError(e))
            } finally {
                context = null
            }
        }
    }
}
