package com.livetyping.facebook

import com.facebook.login.LoginResult
import com.livetyping.logincore.SocialLoginResult


class FacebookLoginResult(val accessToken: String, applicationId: String, userId: String)
    : SocialLoginResult {

}

internal fun LoginResult.toFacebookLoginResult(): FacebookLoginResult {
    return FacebookLoginResult(accessToken.token, accessToken.applicationId, accessToken.userId)
}
