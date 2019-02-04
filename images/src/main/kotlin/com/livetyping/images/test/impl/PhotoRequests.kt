package com.livetyping.images.test.impl

import com.livetyping.images.settings.CachePathSettings
import com.livetyping.images.settings.DefaultTakePhotoSettings
import java.io.File


class PhotoRequestDefaultPath(chooser: String? = null, result: (file: File) -> Unit)
    : PhotoRequest(chooser, DefaultTakePhotoSettings(), result)

class PhotoRequestCachePath(attrName: String,
                            additionalPath: String? = null,
                            fileName: String = "file_in_cache_dir",
                            chooser: String? = null,
                            result: (file: File) -> Unit)
    : PhotoRequest(chooser, CachePathSettings(attrName, additionalPath, fileName), result)



