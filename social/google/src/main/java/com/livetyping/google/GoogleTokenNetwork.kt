package com.livetyping.google

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.livetyping.logincore.SocialLoginError
import com.livetyping.logincore.SocialNetwork


class GoogleTokenNetwork(androidClientId: String, webClientId: String) :
        SocialNetwork<GoogleTokenResult> {

    companion object {
        private const val GOOGLE_SIGN_IN_ACTIVITY_REQUEST_CODE = 231
    }

    private var handlerThread = HandlerThread("GoogleTokenNetwork")
    private lateinit var handler: Handler
    private var context: Context? = null
    private lateinit var account: GoogleSignInAccount
    private lateinit var successBlock: (result: GoogleTokenResult) -> Unit

    private val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(androidClientId)
            .requestId()
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

    override fun login(activity: Activity) {
        context = activity
        val signInClient = GoogleSignIn.getClient(activity, signInOptions)
        activity.startActivityForResult(signInClient.signInIntent, GOOGLE_SIGN_IN_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int, data: Intent?,
                                  successBlock: (result: GoogleTokenResult) -> Unit,
                                  errorBlock: ((error: SocialLoginError) -> Unit)?) {
        this.successBlock = successBlock
        if (requestCode == GOOGLE_SIGN_IN_ACTIVITY_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                account = task.getResult(ApiException::class.java)!!
                handlerThread.start()
                handler = Handler(handlerThread.looper)
                handler.post(loadToken)
            } catch (e: ApiException) {
                errorBlock?.invoke(GoogleLoginError(e))
                context = null
            }
        }
    }

    private val loadToken: () -> Unit = {
        val scopes = "oauth2:profile email"
        val token = GoogleAuthUtil.getToken(context, account.account, scopes)
        successBlock.invoke(GoogleTokenResult(token))
        handler.removeMessages(0)
        context = null
    }
}
