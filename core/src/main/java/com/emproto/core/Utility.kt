package com.emproto.core

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.Log
import android.view.Window
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.io.*
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception


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

    fun compareDates(dateFrom: String): Boolean {
        val c = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val currentDate = format.format(c.time)

        return dateFrom < currentDate

    }

    fun parseDateFromUtc(time: String?, inputDateFormat: String? = null): String? {
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

    fun parseDateFromUtcToMMYYYY(time: String?, inputDateFormat: String? = null): String? {
        val inputPattern: String
        inputPattern = inputDateFormat ?: "yyyy-MM-dd'T'HH:mm:ss.SSS"
        //yyyy-MM-dd HH:mm:ss.SSS
        val outputPattern = "MMM yyyy"
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

    /**
     * Getting bitmap from base64 data
     *
     * @param stringBase64
     * @return
     */
    fun getBitmapFromBase64(stringBase64: String?): Bitmap? {
        try {
            val bytes = Base64.decode(stringBase64, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun writeResponseBodyToDisk(body: String): File? {
        return try {
            // todo change the file location/name according to your needs
            val bytes = Base64.decode(body, Base64.DEFAULT)
            val fileSuffix = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
            val futureStudioIconFile =
                File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    fileSuffix + ".pdf"
                )
            if (!futureStudioIconFile.exists()) {
                futureStudioIconFile.createNewFile()
            } else {
                futureStudioIconFile.delete()
                futureStudioIconFile.createNewFile()
            }
            val inputStream: InputStream =
                ByteArrayInputStream(bytes)

            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                //val fileSize: Long = body.contentLength()
                var fileSizeDownloaded: Long = 0
                //inputStream = body.length
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d("", "file download: $fileSizeDownloaded ")
                }
                outputStream?.flush()
                futureStudioIconFile
            } catch (e: IOException) {
                Log.e("Error in file", e.localizedMessage)
                null
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            Log.e("Error in file", e.localizedMessage)
            null
        }
    }


    fun formatAmount(amount: Double): String {
        val df = DecimalFormat()
        df.maximumFractionDigits = 2
        var amountInString = ""
        try {
            amountInString = if (amount < 100000) {
                "₹ ${df.format(amount)}"
            } else {
                val value = df.format(amount / 100000)
                "₹$value Lakhs*"
            }
        } catch (e: Exception) {
            return ""
        }


        return amountInString
    }

    fun convertToDecimal(value: Double): String {
        val df = DecimalFormat()
        df.maximumFractionDigits = 2
        return df.format(value / 100000)
    }

    fun convertTo(value: Double): String {
        val df = DecimalFormat()
        df.maximumFractionDigits = 2
        return df.format(value)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun convertString(textView: TextView, context: Context, text: String, maxLines: Int) {
        val TWO_SPACES = ""
        textView.text = text
        textView.post(object : Runnable {
            override fun run() {

                // Past the maximum number of lines we want to display.
                if (textView.lineCount > maxLines) {
                    val lastCharShown = textView.layout.getLineVisibleEnd(maxLines - 1) - 5
                    Log.d("SSS", lastCharShown.toString())
                    textView.maxLines = maxLines
                    val moreString = "READ MORE"
                    val suffix = TWO_SPACES + moreString

                    // 3 is a "magic number" but it's just basically the length of the ellipsis we're going to insert
                    val actionDisplayText =
                        text.substring(
                            0,
                            lastCharShown - suffix.length - 2
                        ) + "..." + suffix
                    val truncatedSpannableString = SpannableString(actionDisplayText)
                    val startIndex = actionDisplayText.indexOf(moreString)
                    truncatedSpannableString.setSpan(
                        ForegroundColorSpan(context.getColor(R.color.app_color)),
                        startIndex,
                        startIndex + moreString.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    textView.text = truncatedSpannableString
                }
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun convertStringIns(textView: TextView, context: Context, text: String, maxLines: Int) {
        val TWO_SPACES = ""
        textView.text = text
        textView.post(object : Runnable {
            override fun run() {

                // Past the maximum number of lines we want to display.
                if (textView.lineCount > maxLines) {
                    val lastCharShown = textView.layout.getLineVisibleEnd(maxLines - 1) - 2
                    Log.d("SSS", lastCharShown.toString())
                    textView.maxLines = maxLines
                    val moreString = "READ MORE"
                    val suffix = TWO_SPACES + moreString

                    // 3 is a "magic number" but it's just basically the length of the ellipsis we're going to insert
                    val actionDisplayText =
                        text.substring(
                            0,
                            lastCharShown - suffix.length - 3
                        ) + "..." + suffix
                    val truncatedSpannableString = SpannableString(actionDisplayText)
                    val startIndex = actionDisplayText.indexOf(moreString)
                    truncatedSpannableString.setSpan(
                        ForegroundColorSpan(context.getColor(R.color.app_color)),
                        startIndex,
                        startIndex + moreString.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    textView.text = truncatedSpannableString
                }
            }
        })

    }

    fun getBookingStatus(bookingNumber: Int): String {
        return if (bookingNumber == 225360010) {
            "Booking Done"
        } else {
            "Booking in Progress"
        }
    }


}
