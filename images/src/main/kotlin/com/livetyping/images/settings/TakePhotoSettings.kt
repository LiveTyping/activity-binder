package com.livetyping.images.settings

import android.content.Context
import java.io.File
import java.io.IOException


abstract class TakePhotoSettings {

    abstract val attrName: String

    abstract val attrPath: String?

    abstract val fileName: String

    @Throws(IOException::class)
    internal fun getImageFile(context: Context): File {
        val rootPath = getRootPath(context)
        val path = if (attrPath == null) File(rootPath) else File(rootPath, attrPath)
        if (path.exists().not()) {
            path.mkdirs()
        }
        val file = File(path.absolutePath, "$fileName.jpg")
        if (file.exists().not()) {
            file.createNewFile()
        }
        return file
    }

    internal abstract fun getRootPath(context: Context): String
}
