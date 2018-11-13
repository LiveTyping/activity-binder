package com.livetyping.google

import android.app.Activity
import android.arch.lifecycle.*
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.livetyping.logincore.SocialLoginError
import com.livetyping.logincore.SocialNetwork


class GoogleTokenNetwork(androidClientId: String, webClientId: String) :
        SocialNetwork<GoogleTokenResult>, LoaderManager.LoaderCallbacks<String>, LifecycleOwner, ViewModelStoreOwner {

    override fun getViewModelStore(): ViewModelStore {
        return object : ViewModelStore() {}
    }

    private val registry: LifecycleRegistry by lazy {
        LifecycleRegistry(this)
    }

    override fun getLifecycle(): Lifecycle = registry


    companion object {
        private const val GOOGLE_SIGN_IN_ACTIVITY_REQUEST_CODE = 231
        private const val LOADER_KEY = "GoogleTokenNetwork"
        private const val LOADER_VALUE = "GetGoogleToken"
        private const val LOADER_ID = 134
    }

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
        registry.markState(Lifecycle.State.STARTED)
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
                val loaderBundle = Bundle().apply { putString(LOADER_KEY, LOADER_VALUE) }
                LoaderManager.getInstance(this).initLoader(LOADER_ID, loaderBundle, this).forceLoad()
            } catch (e: ApiException) {
                errorBlock?.invoke(GoogleLoginError(e))
            } finally {
                context = null
            }
        }
    }

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<String> {
        return GoogleTokenLoader(context!!, account)
    }

    override fun onLoadFinished(p0: Loader<String>, p1: String) {
        successBlock.invoke(GoogleTokenResult(p1))
    }

    override fun onLoaderReset(p0: Loader<String>) {
    }

}
