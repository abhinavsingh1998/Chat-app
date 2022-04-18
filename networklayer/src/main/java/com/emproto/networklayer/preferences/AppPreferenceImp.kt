package com.emproto.networklayer.preferences

import android.content.Context
import javax.inject.Inject

class AppPreferenceImp @Inject constructor(context: Context) : AppPreference {

    companion object {
        val LOGGEDIN = "login"
        val TOKEN = "token"
    }

    private var preference = context.getSharedPreferences("hoabl-pref", Context.MODE_PRIVATE)
    private var editor = preference.edit()
    override fun saveLogin(loggedIn: Boolean) {
        saveBoolean(LOGGEDIN, loggedIn)
    }

    override fun isUserLoggedIn(): Boolean {
        return getBoolean(LOGGEDIN, false)
    }

    override fun setToken(token: String) {
        saveString(TOKEN, token)
    }

    override fun getToken(): String {
        return getString(TOKEN, "")
    }

    private fun saveString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    private fun getString(key: String, defaultValue: String = ""): String {
        return preference.getString(key, defaultValue) ?: defaultValue
    }

    private fun saveBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return preference.getBoolean(key, defaultValue)
    }
}