package com.livetyping.google

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.livetyping.logincore.SocialLoginResult


class GoogleLoginResult(val accessToken: String, val email: String) : SocialLoginResult

internal fun GoogleSignInAccount.toSocialResult(): GoogleLoginResult {
    return GoogleLoginResult(idToken!!, email!!)
}
