package com.livetyping.activitybinder

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.auth.GoogleAuthUtil
import com.livetyping.facebook.FacebookNetwork
import com.livetyping.google.GoogleAccountNetwork
import com.livetyping.google.GoogleTokenNetwork
import com.livetyping.instagram.InstagramNetwork
import com.livetyping.logincore.SocialLoginBinder
import com.livetyping.vk.VkNetwork
import kotlinx.android.synthetic.main.activity_social.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SocialActivity : AppCompatActivity() {

    private lateinit var socialLoginBinder: SocialLoginBinder

    private companion object {
        const val GOOGLE_ANDROID_CLIENT_ID = "962786784406-vrmqfde2mtng3vei55djkqehd5me9t42.apps.googleusercontent.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social)

        socialLoginBinder = (application as BinderExampleApplication).socialLoginBinder // can be injected with dagger

        login_vk.setOnClickListener {
            socialLoginBinder.loginWith(VkNetwork()) {
                Toast.makeText(this, it.accessToken, Toast.LENGTH_SHORT).show()
            }
        }

        login_fb.setOnClickListener {
            socialLoginBinder.loginWith(FacebookNetwork()) {
                Toast.makeText(this, it.accessToken, Toast.LENGTH_SHORT).show()
            }
        }
        login_instagram.setOnClickListener {
            socialLoginBinder.loginWith(InstagramNetwork()) {
                Toast.makeText(this, it.accessToken, Toast.LENGTH_SHORT).show()
            }
        }

        login_google.setOnClickListener {
            socialLoginBinder.loginWith(GoogleAccountNetwork(GOOGLE_ANDROID_CLIENT_ID)) {
                GlobalScope.launch(Dispatchers.IO) {
                    val scopes = "oauth2:profile email"
                    val token = GoogleAuthUtil.getToken(this@SocialActivity, it.account.account, scopes)
                    GlobalScope.launch(Dispatchers.Main) {
                        Toast.makeText(this@SocialActivity, token.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        login_google_token.setOnClickListener {
            socialLoginBinder.loginWith(GoogleTokenNetwork(GOOGLE_ANDROID_CLIENT_ID)) {
                Toast.makeText(this, it.accessToken, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        socialLoginBinder.attach(this)
    }

    override fun onStop() {
        socialLoginBinder.detach(this)
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        socialLoginBinder.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

    }
}
