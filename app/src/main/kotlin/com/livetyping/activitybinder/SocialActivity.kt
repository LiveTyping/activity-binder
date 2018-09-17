package com.livetyping.activitybinder

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.livetyping.facebook.FacebookLoginRoute
import com.livetyping.instagram.InstagramLoginRoute
import com.livetyping.logincore.SocialLoginBinder
import com.livetyping.vk.VkLoginRoute
import kotlinx.android.synthetic.main.activity_social.*


class SocialActivity : AppCompatActivity() {

    private lateinit var socialLoginBinder: SocialLoginBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social)

        socialLoginBinder = (application as BinderExampleApplication).socialLoginBinder // can be injected with dagger

        login_vk.setOnClickListener {
            socialLoginBinder.loginWith(VkLoginRoute()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        login_fb.setOnClickListener {
            socialLoginBinder.loginWith(FacebookLoginRoute()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        login_instagram.setOnClickListener {
            socialLoginBinder.loginWith(InstagramLoginRoute()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
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