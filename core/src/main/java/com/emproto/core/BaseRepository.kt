package com.emproto.core

import android.app.Application
import org.json.JSONObject

abstract class BaseRepository(val application: Application) {
    fun getErrorMessage(body: String): String {
        var message = "Bad Request"
        val jObjError = JSONObject(body)
        message = jObjError.getString("message")
        return message
    }
}
