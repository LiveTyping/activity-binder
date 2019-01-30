package com.livetyping.images.settings

import android.content.Context
import java.io.File


internal class CachePathSettings(override val providerAuthority: String,
                                 override val attrName: String,
                                 override val attrPath: String?,
                                 override val fileName: String = "tempFileInCashDir")
    : TakePhotoSettings() {


    override fun getFilePath(context: Context): File {
        return if (attrPath == null) File(context.cacheDir.path) else File(context.filesDir, attrPath)
    }

}
