package com.livetyping.logincore

import android.app.Application
import android.content.Intent
import com.livetyping.core.Binder


class SocialLoginBinder : Binder() {

    private var route: SocialNetwork? = null
    private var tokenBlock: ((token: String) -> Unit)? = null
    private var errorBlock: ((error: SocialLoginError) -> Unit)? = null

    fun initializeNetworks(app: Application, initializers: Collection<SocialInitializer>) {
        initializers.forEach { it.init(app) }
    }

    fun loginWith(route: SocialNetwork,
                  errorBlock: ((error: SocialLoginError) -> Unit)? = null,
                  successBlock: (token: String) -> Unit) {
        getAttachedObject()?.let {
            this.route = route
            this.tokenBlock = successBlock
            this.errorBlock = errorBlock
            route.login(it)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        route?.onActivityResult(requestCode, resultCode, data,
                { tokenBlock?.invoke(it) }, // success
                { errorBlock?.invoke(it) }) // error
    }
}