package com.livetyping.core.holder.activity

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*

class ActivityHolderImpl : ActivityHolder {
    private var currentActivity: WeakReference<out Activity>? = null
    private val activityHolder = WeakHashMap<Activity, Any>()

    override fun add(activity: Activity?) {
        if (activity != null) {
            saveActivity(activity)
        }
    }

    override fun remove(activity: Activity?) {
        if (activityHolder.remove(activity) != null) {
            savePreviousAcivity()
        }
    }

    override fun getCurrentActivity(): Activity? = currentActivity?.get()

    private fun savePreviousAcivity() {
        val iterator = activityHolder.keys.iterator()
        currentActivity = if (iterator.hasNext()) {
            WeakReference(iterator.next())
        } else {
            null
        }
    }

    private fun saveActivity(activity: Activity) {
        val weakReference = WeakReference(activity)
        activityHolder[activity] = weakReference
        currentActivity = weakReference
    }
}