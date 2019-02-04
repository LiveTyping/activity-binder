package com.livetyping.images.test.impl

import com.livetyping.images.settings.DefaultTakePhotoSettings
import java.io.File


class DefaultPhotoRequest(chooser: String? = null, result: (file: File) -> Unit)
    : PhotoRequest(chooser, DefaultTakePhotoSettings(), result)
