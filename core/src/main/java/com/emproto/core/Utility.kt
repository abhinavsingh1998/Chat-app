package com.emproto.core

import android.app.Activity
import android.app.Dialog
import android.view.Window
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Utility {
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

    fun parseDateFromUtc(time: String?, inputDateFormat: String?): String? {
        val inputPattern: String
        inputPattern = inputDateFormat ?: "yyyy-MM-dd'T'HH:mm:ss.SSS"
        //yyyy-MM-dd HH:mm:ss.SSS
        val outputPattern = "dd MMM yyyy"
        val inputFormat = SimpleDateFormat(inputPattern, Locale.US)
        val outputFormat = SimpleDateFormat(outputPattern)
        val date: Date?
        var str: String? = null
        return try {
            date = inputFormat.parse(time)
            if (date != null) {
                str = outputFormat.format(date)
            }
            str ?: time
        } catch (e: ParseException) {
            e.printStackTrace()
            time
        }
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

    private val c = charArrayOf('K', 'M', 'B', 'T')

    /**
     * Recursive implementation, invokes itself for each factor of a thousand, increasing the class on each invokation.
     * @param n the number to format
     * @param iteration in fact this is the class from the array c
     * @return a String representing the number n formatted in a cool looking way.
     */
     fun coolFormat(n: Double, iteration: Int): String? {
        val d = n.toLong() / 100 / 10.0
        val isRound =
            d * 10 % 10 == 0.0 //true if the decimal part is equal to 0 (then it's trimmed anyway)
        return (if (d < 1000) //this determines the class, i.e. 'k', 'm' etc
            (if (d > 99.9 || isRound || !isRound && d > 9.99) //this decides whether to trim the decimals
                d.toInt() * 10 / 10 else d.toString() + "" // (int) d * 10 / 10 drops the decimal
                    ).toString() + "" + c[iteration] else coolFormat(d, iteration + 1))
    }


}
