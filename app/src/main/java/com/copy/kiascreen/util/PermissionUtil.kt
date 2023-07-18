package com.copy.kiascreen.util

import android.Manifest

class PermissionUtil {
    companion object {
        val PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS , Manifest.permission.CALL_PHONE, Manifest.permission.POST_NOTIFICATIONS)
    }
}