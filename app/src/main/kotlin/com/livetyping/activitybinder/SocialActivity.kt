package com.livetyping.activitybinder

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.livetyping.facebook.FacebookNetwork
import com.livetyping.google.GoogleNetwork
import com.livetyping.instagram.InstagramNetwork
import com.livetyping.logincore.SocialLoginBinder
import com.livetyping.vk.VkNetwork
import kotlinx.android.synthetic.main.activity_social.*


class SocialActivity : AppCompatActivity() {

    private lateinit var socialLoginBinder: SocialLoginBinder

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
            socialLoginBinder.loginWith(GoogleNetwork("some_temp_app")) {
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
