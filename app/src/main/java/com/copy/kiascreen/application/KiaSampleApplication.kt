package com.copy.kiascreen.application

import android.app.Application
import com.copy.kiascreen.util.SharedPreferenceUtil
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltAndroidApp
class KiaSampleApplication : Application() {

    companion object {
        lateinit var prefs : SharedPreferenceUtil
    }


    override fun onCreate() {
        super.onCreate()
        prefs = SharedPreferenceUtil(applicationContext)
    }
}