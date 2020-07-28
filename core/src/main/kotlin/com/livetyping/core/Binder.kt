package com.livetyping.core

import android.app.Activity
import com.livetyping.core.holder.activity.ActivityHolder
import com.livetyping.core.holder.activity.ActivityHolderImpl
import java.lang.ref.WeakReference
import java.util.*


abstract class Binder : SimpleActivityLifecycleCallbacks() {
    private val activityHolder: ActivityHolder = ActivityHolderImpl()

    override fun onActivityStarted(activity: Activity?) {
        activityHolder.add(activity)
    }

    override fun onActivityStopped(activity: Activity?) {
        activityHolder.remove(activity)
    }

    fun getCurrentActivity(): Activity? = activityHolder.getCurrentActivity()
}
