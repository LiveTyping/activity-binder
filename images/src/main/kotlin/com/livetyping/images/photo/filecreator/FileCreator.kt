package com.livetyping.images.photo.filecreator

import android.content.Context
import androidx.annotation.XmlRes
import org.xmlpull.v1.XmlPullParser
import java.io.File
import java.io.IOException


abstract class FileCreator(
        private val attrName: String,
        private val additionalPath: String? = null,
        private val fileName: String
) {

    protected abstract val pathAttr: String
    private lateinit var imageFile: File


    internal abstract fun getRootPath(context: Context): String

    @Throws(IllegalStateException::class)
    internal fun getImageFile(context: Context, @XmlRes paths: Int): File {
        if (::imageFile.isInitialized) {
            return imageFile
        } else {
            val xmlParser = context.resources.getXml(paths)
            while (xmlParser.eventType != XmlPullParser.END_DOCUMENT) {
                if (xmlParser.eventType == XmlPullParser.END_TAG) {
                    xmlParser.next()
                    continue
                }
                val pathName = xmlParser.getAttributeValue(null, "name")
                if (pathName != null && pathName == attrName && xmlParser.name == pathAttr) {
                    val attrPath = xmlParser.getAttributeValue(null, "path")
                    xmlParser.close()
                    imageFile = try {
                        getImageFile(context, attrPath)
                    } catch (e: IOException) {
                        throw IOException(e)
                    }
                    return imageFile
                }
                xmlParser.next()
            }
            xmlParser.close()
            throw IllegalArgumentException("can`t find attr with name '$attrName' " +
                    "in '${context.resources.getResourceEntryName(paths)}' xml file")
        }
    }

    @Throws(IOException::class)
    private fun getImageFile(context: Context, attrPath: String): File {
        val rootPath = getRootPath(context)
        val path = if (additionalPath == null) File(rootPath, attrPath) else {
            File(File(rootPath, attrPath), additionalPath)
        }
        if (path.exists().not()) {
            path.mkdirs()
        }
        val file = File(path.absolutePath, "$fileName.jpg")
        if (file.exists().not()) {
            file.createNewFile()
        }
        return file
    }
}
