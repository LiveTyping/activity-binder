package com.livetyping.activitybinder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissions.setOnClickListener {
            open(PermissionExampleActivity::class.java)
        }
        social.setOnClickListener {
            open(SocialActivity::class.java)
        }
    }

    private fun open(clazz: Class<out Activity>) {
        startActivity(Intent(this, clazz))
    }
}
