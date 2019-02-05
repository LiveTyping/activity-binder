package com.livetyping.google

import android.content.Context
import android.os.AsyncTask
import androidx.loader.content.AsyncTaskLoader
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignInAccount


internal class GoogleTokenLoader(context: Context,
                                 private val googleSignInAccount: GoogleSignInAccount) : AsyncTaskLoader<String>(context) {

    override fun loadInBackground(): String? {
        val scopes = "oauth2:profile email"
        return GoogleAuthUtil.getToken(context, googleSignInAccount.account, scopes)
    }
}

internal class GoogleTokenAsyncTask() : AsyncTask<GoogleSignInAccount, Void, String>() {

    override fun doInBackground(vararg p0: GoogleSignInAccount?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
