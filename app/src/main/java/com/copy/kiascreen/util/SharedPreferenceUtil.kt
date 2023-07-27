package com.copy.kiascreen.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtil(private val context : Context) {
    private val PREFS_NAME = "prefs"
    private val PREFS_PERMISSIONS ="permissions"
    private val PREF_LAUNCH = "initialLaunch"
    private val PREFS_PUSH = "push"
    private val PREFS_AUTO = "autoLogin"
    private val PREF_COACH = "coach"
    private val PREF_MAIN_COACH = "mainCoach"
    private val PREF_LOGIN = "logIn"
    private val PREF_LOGIN_ID = "loginId"
    private val PREF_FB_TOKEN = "fcbToken"

    private val prefs : SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

    var permissions : String
        get() = prefs.getString(PREFS_PERMISSIONS, " ")!!
        set(value) = prefs.edit().putString(PREFS_PERMISSIONS, value).apply()

    var pushFlag : Boolean
        get() = prefs.getBoolean(PREFS_PUSH, true)
        set(value) = prefs.edit().putBoolean(PREFS_PUSH, value).apply()

    var autoLoginFlag : Boolean
        get() = prefs.getBoolean(PREFS_AUTO, false)
        set(value) = prefs.edit().putBoolean(PREFS_AUTO, value).apply()

    var autoLoginId : String?
        get() = prefs.getString(PREF_LOGIN_ID, "")
        set(value) =  prefs.edit().putString(PREF_LOGIN_ID, value).apply()

    var coachFlag : Boolean
        get() = prefs.getBoolean(PREF_COACH, false)
        set(value) = prefs.edit().putBoolean(PREF_COACH, value).apply()

    var mainCoachFlag : Boolean
        get() = prefs.getBoolean(PREF_MAIN_COACH, false)
        set(value) = prefs.edit().putBoolean(PREF_MAIN_COACH, value).apply()

    var loginFlag : Boolean
        get() = prefs.getBoolean(PREF_LOGIN, false)
        set(value) = prefs.edit().putBoolean(PREF_LOGIN, value).apply()

    var fcmToken : String?
        get() = prefs.getString(PREF_FB_TOKEN, "")
        set(value) = prefs.edit().putString(PREF_FB_TOKEN, value).apply()

    var initialLaunch : Boolean
        get() = prefs.getBoolean(PREF_LAUNCH, true)
        set(value) = prefs.edit().putBoolean(PREF_LAUNCH, value).apply()
}