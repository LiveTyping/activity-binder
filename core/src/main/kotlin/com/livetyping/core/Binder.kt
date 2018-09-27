package com.livetyping.core

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*


abstract class Binder {

    private var attachedObjRef: WeakReference<out Activity>? = null
    private val weakHashMap = WeakHashMap<Activity, Any>()

    open fun attach(obj: Activity) {
        val weakReference = WeakReference(obj)
        weakHashMap.put(obj, weakReference)
        attachedObjRef = weakReference
    }

    open fun detach(obj: Activity) {
        if (weakHashMap.remove(obj) == null) {
            return
        }
        val iterator = weakHashMap.keys.iterator()
        attachedObjRef = if (iterator.hasNext()) {
            WeakReference(iterator.next())
        } else null
    }

    fun getAttachedObject(): Activity? {
        return attachedObjRef?.get()
    }

}