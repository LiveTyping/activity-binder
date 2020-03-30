package com.livetyping.activitybinder

import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule

class MainActivityTestRule: ActivityTestRule<MainActivity>(MainActivity::class.java, true, false) {

    override fun getActivityIntent(): Intent {
        val intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return intent
    }
}