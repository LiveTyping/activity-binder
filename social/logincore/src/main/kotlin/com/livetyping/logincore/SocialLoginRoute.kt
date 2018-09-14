package com.livetyping.utils.social

import android.app.Activity
import android.content.Intent


interface SocialLoginRoute {

    fun login(activity: Activity)

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?,
                         successBlock: (token: String) -> Unit,
                         errorBlock: ((error: SocialLoginError) -> Unit)? = null)
}