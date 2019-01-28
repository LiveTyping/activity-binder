package com.livetyping.images.settings

import android.content.Context
import java.io.File


internal class DefaultTakePhotoSettings : TakePhotoSettings() {

    override val providerAuthority: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val tagPath: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val attrName: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val attrPath: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val fileName: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getFilePath(context: Context): File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
