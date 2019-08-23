package com.livetyping.logincore

import android.app.Application
import android.content.Intent
import com.livetyping.core.Binder

class SocialLoginBinder : Binder() {

    private lateinit var socialNetwork: SocialNetwork<out SocialLoginResult>
    private lateinit var onSuccess: (result: SocialLoginResult) -> Unit
    private lateinit var onFail: (error: Exception) -> Unit

    fun initializeNetworks(app: Application, initializers: Collection<SocialInitializer>) {
        initializers.forEach { it.init(app) }
    }

    fun <T : SocialLoginResult> loginWith(
        socialNetwork: SocialNetwork<T>,
        onSuccess: (result: T) -> Unit,
        onFail: (exception: Exception) -> Unit
    ) {
        getAttachedObject()?.let {
            this.socialNetwork = socialNetwork
            this.onSuccess = onSuccess as (SocialLoginResult) -> Unit
            this.onFail = onFail
            socialNetwork.login(it)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        socialNetwork.onActivityResult(
            requestCode,
            resultCode,
            data,
            onSuccess,
            onFail
        )
    }
}
