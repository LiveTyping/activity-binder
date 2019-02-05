package com.livetyping.permission

import android.Manifest


internal class PermissionRequestCodes() {
    companion object {
        const val MULTIPLE_PERMISSIONS_CODE = 116
    }

    fun getCode(permission: String): Int {
        val permissionCode = when (permission) {
            Manifest.permission.CAMERA -> 101
            Manifest.permission.READ_CALENDAR -> 102
            Manifest.permission.WRITE_CALENDAR -> 103
            Manifest.permission.READ_CONTACTS -> 104
            Manifest.permission.WRITE_CONTACTS -> 105
            Manifest.permission.GET_ACCOUNTS -> 106
            Manifest.permission.ACCESS_FINE_LOCATION -> 107
            Manifest.permission.ACCESS_COARSE_LOCATION -> 108
            Manifest.permission.RECORD_AUDIO -> 109
            Manifest.permission.READ_PHONE_STATE -> 110
            Manifest.permission.CALL_PHONE -> 111
            Manifest.permission.READ_CALL_LOG -> 112
            Manifest.permission.WRITE_CALL_LOG -> 113
            Manifest.permission.ADD_VOICEMAIL -> 114
            Manifest.permission.USE_SIP -> 115
            Manifest.permission.PROCESS_OUTGOING_CALLS -> 116
            Manifest.permission.BODY_SENSORS -> 117
            Manifest.permission.SEND_SMS -> 118
            Manifest.permission.RECEIVE_SMS -> 119
            Manifest.permission.READ_SMS -> 120
            Manifest.permission.RECEIVE_WAP_PUSH -> 121
            Manifest.permission.RECEIVE_MMS -> 112
            Manifest.permission.READ_EXTERNAL_STORAGE -> 113
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> 115
            else -> {
                throw IllegalArgumentException("PermissionRequestCodes, undefined permission")
            }
        }
        if (permissionCode == MULTIPLE_PERMISSIONS_CODE) throw IllegalStateException("Please, choose another request code!")
        return permissionCode
    }

}