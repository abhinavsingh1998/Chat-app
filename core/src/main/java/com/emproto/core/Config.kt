package com.emproto.core

import android.app.Activity
import android.app.Dialog
import android.view.Window
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Config {
    const val START_DATE = "startdate"
    const val END_DATE = "enddate"
    const val FILTER_SELECTED = "filter"

    fun parseDate(time: String?): String? {
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "dd MMM yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    fun getEncryptedString(value: String): String {
        if (value.length > 4) {
            val firstStr = StringBuilder()
            for (i in 0 until value.length - 4) {
                firstStr.append("*")
            }
            return firstStr.toString() + value.substring(value.length - 4, value.length)
        }
        return value
    }

    fun showDialogPermissions(activity: Activity?, msg: String?) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
       // val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
      //  text.text = msg
       // val dialogButton: Button = dialog.findViewById<View>(R.id.btn_dialog) as Button
        dialog.show()
    }


}
