package com.livetyping.images.settings

import android.content.Context
import androidx.annotation.XmlRes
import org.xmlpull.v1.XmlPullParser
import java.io.File
import java.io.IOException


abstract class TakePhotoSettings {

    abstract val attrName: String

    abstract val additionalPath: String?

    abstract val fileName: String

    protected abstract val pathAttr: String

    @Throws(IOException::class, IllegalStateException::class)
    internal fun getImageFile(context: Context): File {
        val rootPath = getRootPath(context)
        val path = if (additionalPath == null) File(rootPath) else File(rootPath, additionalPath)
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

    internal fun getImageFile(context: Context, @XmlRes paths: Int): File {
        val xmlParser = context.resources.getXml(paths)
        while (xmlParser.eventType != XmlPullParser.END_DOCUMENT) {
            if (xmlParser.eventType == XmlPullParser.END_TAG) {
                xmlParser.next()
                continue
            }
            val pathName = xmlParser.getAttributeValue(null, "name")
            if (pathName != null && pathName == attrName && xmlParser.name == pathName) {
                xmlParser.close()
            }
            xmlParser.next()
            return getImageFile(context)
        }
        xmlParser.close()
        throw IllegalArgumentException("can`t find attr with name '$attrName' " +
                "in '${context.resources.getResourceEntryName(paths)}' xml file")
    }
}
