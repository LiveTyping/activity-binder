package com.livetyping.images.settings

import android.content.Context
import androidx.annotation.XmlRes
import org.xmlpull.v1.XmlPullParser


internal class FilePathFactory(@XmlRes private val paths: Int) {

    fun createSettings(context: Context, attrName: String): TakePhotoSettings {
        val xmlParser = context.resources.getXml(paths)
        while (xmlParser.eventType != XmlPullParser.END_DOCUMENT) {
            if (xmlParser.eventType == XmlPullParser.END_TAG) {
                xmlParser.next()
                continue
            }
            val pathName = xmlParser.getAttributeValue(null, "name")
            if (pathName != null && pathName == attrName) {
                val takePhotoSettingsByParser = getTakePhotoSettingsByParser(xmlParser, attrName)
                xmlParser.close()
                return takePhotoSettingsByParser
            }
            xmlParser.next()
        }
        xmlParser.close()
        throw IllegalArgumentException("can`t find attr with name '$attrName' " +
                "in '${context.resources.getResourceEntryName(paths)}' xml file")
    }

    private fun getTakePhotoSettingsByParser(parser: XmlPullParser, attrName: String): TakePhotoSettings {
        val attributePath = parser.getAttributeValue(null, "path")
        val attrPath = if (attributePath == "." || attributePath == "/") null else attributePath
        return when (parser.name) {
            "files-path" -> FilesPathSettings(attrName, attrPath)
            "cache-path" -> CachePathSettings(attrName, attrPath)
            "external-path" -> ExternalPathSettings(attrName, attrPath)
            "external-files-path" -> ExternalFilesPathSettings(attrName, attrPath)
            "external-cache-path" -> ExternalCachePathSettings(attrName, attrPath)
            else -> DefaultTakePhotoSettings()
        }
    }

}
