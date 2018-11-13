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


class VkNetwork : SocialNetwork<VkLoginResult> {

    override fun login(activity: Activity) {
        VKSdk.login(activity, VKScope.EMAIL)
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int, data: Intent?,
                                  successBlock: (result: VkLoginResult) -> Unit,
                                  errorBlock: ((error: SocialLoginError) -> Unit)?) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                    override fun onResult(vkAccessToken: VKAccessToken) {
                        successBlock.invoke(vkAccessToken.toVkLoginResult())
                    }

                    override fun onError(error: VKError) {
                        errorBlock?.invoke(VkLoginError(error))
                    }
                })) {
        }
    }
}
