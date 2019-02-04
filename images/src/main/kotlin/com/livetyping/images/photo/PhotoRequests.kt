package com.livetyping.images.photo

import com.livetyping.images.photo.filecreator.*
import java.io.File


/**
 * crete file in data/data/{applicationId}/files
 */
class PhotoRequestDefaultPath(chooser: String? = null, result: (file: File) -> Unit)
    : PhotoRequest(chooser, DefaultFileCreator(), result)

/**
 * crete file in data/data/{applicationId}/cache/additionalPath
 */
class PhotoRequestCachePath(attrName: String,
                            additionalPath: String? = null,
                            fileName: String = "file_in_cache_dir",
                            chooser: String? = null,
                            result: (file: File) -> Unit)
    : PhotoRequest(chooser, CachePathCreator(attrName, additionalPath, fileName), result)

/**
 * crete file in /storage/emulated/0/Android/data/{applicationId}/cache/additionalPath
 */
class PhotoRequestExternalCachePath(attrName: String,
                                    additionalPath: String? = null,
                                    fileName: String = "file_in_external_cache_dir",
                                    chooser: String? = null,
                                    result: (file: File) -> Unit)
    : PhotoRequest(chooser, ExternalCachePathCreator(attrName, additionalPath, fileName), result)

/**
 * crete file in storage/emulated/0/Android/{applicationId}/files/additionalPath
 */
class PhotoRequestExternalFilesPath(attrName: String,
                                    additionalPath: String? = null,
                                    fileName: String = "file_in_external_files_dir",
                                    chooser: String? = null,
                                    result: (file: File) -> Unit)
    : PhotoRequest(chooser, ExternalFilesPathCreator(attrName, additionalPath, fileName), result)

/**
 * crete file in storage/emulated/0/files/additionalPath
 */
class PhotoRequestExternalPath(attrName: String,
                               additionalPath: String? = null,
                               fileName: String = "file_in_external_dir",
                               chooser: String? = null,
                               result: (file: File) -> Unit)
    : PhotoRequest(chooser, ExternalPathCreator(attrName, additionalPath, fileName), result)
/**
 * crete file in data/data/{applicationId}/files/additionalPath
 */
class PhotoRequestFilesPath(attrName: String,
                            additionalPath: String? = null,
                            fileName: String = "file_in_app_files_dir",
                            chooser: String? = null,
                            result: (file: File) -> Unit)
    : PhotoRequest(chooser, FilesPathCreator(attrName, additionalPath, fileName), result)
