package com.livetyping.activitybinder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
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
    }
}