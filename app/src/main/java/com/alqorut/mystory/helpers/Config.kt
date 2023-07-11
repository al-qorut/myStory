package com.alqorut.mystory.helpers

import android.content.Context
import android.widget.Toast

class Config(context: Context) {
    companion object{
        fun newInstance(context: Context) = Config(context)
    }

    val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    var isAppFirstRun :Boolean
        get() = prefs.getBoolean(APP_FIRST_RUN, true)
        set(show) = prefs.edit().putBoolean(APP_FIRST_RUN, show).apply()

    var isLogin : String?
        get() = prefs.getString(USER_IS_LOGIN, "")
        set(login) = prefs.edit().putString(USER_IS_LOGIN, login).apply()

    var userLogin : Boolean
        get() = prefs.getBoolean(IS_LOGIN, false)
        set(login) = prefs.edit().putBoolean(IS_LOGIN, login).apply()

    var token : String?
        get() = prefs.getString(TOKEN_KEY, "")
        set(token) = prefs.edit().putString(TOKEN_KEY, token).apply()

    var userId : String?
        get() = prefs.getString(USER_ID, "")
        set(userid) = prefs.edit().putString(USER_ID, userid).apply()

    var nama : String?
        get() = prefs.getString(USER_NAME, "")
        set(name) = prefs.edit().putString(USER_NAME, name).apply()

}