package com.livetyping.images.photo.settings

import android.content.Context

/**
 * crete file in data/data/{applicationId}/files
 */
internal class DefaultTakePhotoSettings
    : TakePhotoSettings("images", null, "file_in_app_files_dir") {

    override val pathAttr: String
        get() = "files-path"


    override fun getRootPath(context: Context) = context.filesDir.path


}
