package com.emproto.core

import android.app.Application
import com.emproto.networklayer.ApiConstants
import org.json.JSONObject

abstract class BaseRepository(val application: Application) {
    fun getErrorMessage(body: String): String {
        var message = "Bad Request"
        val jObjError = JSONObject(body)
        message = jObjError.getString("message")
        return message
    }

    fun getErrorMessage(e: Exception): String {
        return if (e.message == ApiConstants.NO_INTERNET) {
            e.message!!
        } else {
            e.message!!
        }
    }
}
