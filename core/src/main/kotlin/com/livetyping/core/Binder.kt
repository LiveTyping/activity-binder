package com.livetyping.core

import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference
import java.util.*


abstract class Binder {

    private var attachedObjRef: WeakReference<out AppCompatActivity>? = null
    private val weakHashMap = WeakHashMap<AppCompatActivity, Any>()

    open fun attach(obj: AppCompatActivity) {
        val weakReference = WeakReference(obj)
        weakHashMap[obj] = weakReference
        attachedObjRef = weakReference
    }

    open fun detach(obj: AppCompatActivity) {
        if (weakHashMap.remove(obj) == null) {
            return
        }
        val iterator = weakHashMap.keys.iterator()
        attachedObjRef = if (iterator.hasNext()) {
            WeakReference(iterator.next())
        } else null
    }

    fun getAttachedObject(): AppCompatActivity? {
        return attachedObjRef?.get()
    }
}
