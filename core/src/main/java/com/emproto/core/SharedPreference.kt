package com.emproto.core

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreference {

    private var mSharedPref: SharedPreferences? = null

    @Inject
    fun init(context: Context): SharedPreferences? {
        return if (mSharedPref == null) {
            context.getSharedPreferences(Constants.PREF_NAME, Activity.MODE_PRIVATE).also {
                mSharedPref = it
            }
        } else {
            mSharedPref
        }
    }

    fun read(key: String?, defValue: String?): String? {
        return mSharedPref!!.getString(key, defValue)
    }

    fun write(key: String?, value: String?) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun remove(key: String?) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.remove(key)
        prefsEditor.apply()
    }

    fun read(key: String?, defValue: Boolean): Boolean {
        return mSharedPref!!.getBoolean(key, defValue)
    }

    fun write(key: String?, value: Boolean) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.commit()
    }

    fun clear() {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.clear()
        prefsEditor.commit()
    }

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        val editor=mSharedPref!!.edit()
        editor!!.putBoolean(Constants.IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor!!.commit()
    }

    fun isFirstTimeLaunch(): Boolean {
        return mSharedPref!!.getBoolean(Constants.IS_FIRST_TIME_LAUNCH, true)
    }

}