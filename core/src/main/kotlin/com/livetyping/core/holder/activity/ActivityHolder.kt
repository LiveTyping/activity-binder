package com.livetyping.core.holder.activity

import android.app.Activity

interface ActivityHolder {
    fun add(activity: Activity?)

    fun remove(activity: Activity?)

    fun getCurrentActivity(): Activity?
}
