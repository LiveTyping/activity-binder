package com.livetyping.images.settings

import android.content.Context
import java.io.File


/**
 * crete file in data/data/{applicationId}/cache/attrPath
 */
internal class CachePathSettings(override val attrName: String,
                                 override val attrPath: String?,
                                 override val fileName: String = "tempFileInCashDir")
    : TakePhotoSettings() {


    override fun getRootPath(context: Context) = context.cacheDir.path

}
