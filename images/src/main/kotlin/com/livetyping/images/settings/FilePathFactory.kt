package com.livetyping.images.settings

import android.content.Context
import androidx.annotation.XmlRes
import org.xmlpull.v1.XmlPullParser


internal class FilePathFactory(private val providerAuthority: String, @XmlRes private val paths: Int) {

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
        throw IllegalArgumentException("can`t find attr with name $attrName " +
                "in ${context.resources.getResourceEntryName(paths)} xml file")
    }

    private fun getTakePhotoSettingsByParser(parser: XmlPullParser, attrName: String): TakePhotoSettings {
        val attributePath = parser.getAttributeValue(null, "path")
        val attrPath = if (attributePath == "." || attributePath == "/") null else attributePath
        return when (parser.name) {
            "files-path" -> FilesPathSettings(providerAuthority, attrName, attrPath)
            "cache-path" -> CachePathSettings(providerAuthority, attrName, attrPath)
            "external-path" -> ExternalPathSettings(providerAuthority, attrName, attrPath)
            "external-files-path" -> ExternalFilesPathSettings(providerAuthority, attrName, attrPath)
            else -> DefaultTakePhotoSettings()
        }
    }

}
