package com.livetyping.google

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.livetyping.logincore.SocialLoginResult


class GoogleLoginResult(val account: GoogleSignInAccount) : SocialLoginResult

internal fun GoogleSignInAccount.toSocialResult(): GoogleLoginResult {
    return GoogleLoginResult(this)
}
