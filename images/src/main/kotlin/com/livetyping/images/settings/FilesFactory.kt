package com.livetyping.images.settings

import android.content.Context
import androidx.annotation.XmlRes
import org.xmlpull.v1.XmlPullParser


internal class FilesFactory(private val providerAuthority: String, @XmlRes private val paths: Int) {

    fun createSettings(context: Context, attrName: String): TakePhotoSettings {
        val xmlParser = context.resources.getXml(paths)
        val event = xmlParser.eventType
        while (event != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.START_TAG) {
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
        throw IllegalArgumentException("")
    }

    private fun getTakePhotoSettingsByParser(parser: XmlPullParser, attrName: String): TakePhotoSettings {
        return when (parser.name) {
            "files-path" -> FilesPathSettings(providerAuthority, attrName, parser.getAttributeValue(null, "path"))
            else -> DefaultTakePhotoSettings()
        }
    }

}
