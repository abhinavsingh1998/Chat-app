package com.emproto.hoabl.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


object Extensions{

    fun saveToPreferences(activity:Activity,key:String,value:String){
        val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getFromPreferences(activity:Activity,key:String,value:String):String{
        val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(key,"").toString()
    }
}