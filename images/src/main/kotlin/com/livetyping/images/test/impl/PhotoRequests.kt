package com.livetyping.images.test.impl

import com.livetyping.images.settings.*
import java.io.File


/**
 * crete file in data/data/{applicationId}/files
 */
class PhotoRequestDefaultPath(chooser: String? = null, result: (file: File) -> Unit)
    : PhotoRequest(chooser, DefaultTakePhotoSettings(), result)

/**
 * crete file in data/data/{applicationId}/cache/additionalPath
 */
class PhotoRequestCachePath(attrName: String,
                            additionalPath: String? = null,
                            fileName: String = "file_in_cache_dir",
                            chooser: String? = null,
                            result: (file: File) -> Unit)
    : PhotoRequest(chooser, CachePathSettings(attrName, additionalPath, fileName), result)

/**
 * crete file in /storage/emulated/0/Android/data/{applicationId}/cache/additionalPath
 */
class PhotoRequestExternalCachePath(attrName: String,
                                    additionalPath: String? = null,
                                    fileName: String = "file_in_external_cache_dir",
                                    chooser: String? = null,
                                    result: (file: File) -> Unit)
    : PhotoRequest(chooser, ExternalCachePathSettings(attrName, additionalPath, fileName), result)

/**
 * crete file in storage/emulated/0/Android/{applicationId}/files/additionalPath
 */
class PhotoRequestExternalFilesPath(attrName: String,
                                    additionalPath: String? = null,
                                    fileName: String = "file_in_external_files_dir",
                                    chooser: String? = null,
                                    result: (file: File) -> Unit)
    : PhotoRequest(chooser, ExternalFilesPathSettings(attrName, additionalPath, fileName), result)

/**
 * crete file in storage/emulated/0/files/additionalPath
 */
class PhotoRequestExternalPath(attrName: String,
                               additionalPath: String? = null,
                               fileName: String = "file_in_external_dir",
                               chooser: String? = null,
                               result: (file: File) -> Unit)
    : PhotoRequest(chooser, ExternalPathSettings(attrName, additionalPath, fileName), result)
/**
 * crete file in data/data/{applicationId}/files/additionalPath
 */
class PhotoRequestFilesPath(attrName: String,
                            additionalPath: String? = null,
                            fileName: String = "file_in_app_files_dir",
                            chooser: String? = null,
                            result: (file: File) -> Unit)
    : PhotoRequest(chooser, FilesPathSettings(attrName, additionalPath, fileName), result)
