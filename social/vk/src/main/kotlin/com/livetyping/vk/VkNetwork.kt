package com.livetyping.vk

import android.app.Activity
import android.content.Intent
import com.livetyping.logincore.SocialLoginError
import com.livetyping.logincore.SocialNetwork
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError


class VkNetwork : SocialNetwork {
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?,
                                  successBlock: (token: String) -> Unit,
                                  errorBlock: ((SocialLoginError) -> Unit)?) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                    override fun onResult(res: VKAccessToken) {
                        successBlock.invoke(res.accessToken)
                    }

                    override fun onError(error: VKError) {
                        errorBlock?.invoke(VkLoginError(error))
                    }
                })) {
        }
    }

    override fun login(activity: Activity) {
        VKSdk.login(activity, VKScope.EMAIL)
    }

}
