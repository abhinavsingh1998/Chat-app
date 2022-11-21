package com.emproto.core

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
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
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object Utility {

    const val START_DATE = "startdate"
    const val END_DATE = "enddate"
    const val FILTER_SELECTED = "filter"

    fun dateInWords(time: String): String? {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat: SimpleDateFormat
        var date: Date? = null
        var str: String? = null
        try {
            outputFormat = if (time.endsWith("1") && !time.endsWith("11"))
                SimpleDateFormat("d'st' MMMM yyyy")
            else if (time.endsWith("2") && !time.endsWith("12"))
                SimpleDateFormat("d'nd' MMMM  yyyy")
            else if (time.endsWith("3") && !time.endsWith("13"))
                SimpleDateFormat("d'rd' MMMM yyyy")
            else SimpleDateFormat("d'th' MMMM yyyy")

            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    fun parseDate(time: String?): String? {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val outputPattern = "dd/MM/yyyy"
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
        df.roundingMode = RoundingMode.CEILING
        var amountInString = ""
        try {
            amountInString = if (amount < 100000) {
                "₹ ${df.format(amount)}"
            } else if (amount > 10000000) {
                val value = df.format(amount / 10000000)
                "₹$value Cr*"
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
        df.roundingMode = RoundingMode.CEILING
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

    fun convertDate(time: String?, inputDateFormat: String? = null): String? {
        val inputPattern: String
        inputPattern = inputDateFormat ?: "dd/MM/yyyy"
        //yyyy-MM-dd HH:mm:ss.SSS
        val outputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS"
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

    fun convertDateToTime(date: String): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        return df.parse(date).hours.toString()
    }

    fun getCompressedImageFile(cameraFile: File, context: Context?): File? {
        try {
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            if ((cameraFile!!.name).equals(Constants.JPG_SMALL) || (cameraFile!!.name).equals(
                    Constants.JPG_CAPITAL
                ) || (cameraFile!!.name).equals(
                    Constants.PNG_SMALL
                ) || (cameraFile!!.name).equals(Constants.PNG_CAPITAL) || (cameraFile!!.name).equals(
                    Constants.JPEG_SMALL
                ) || (cameraFile!!.name).equals(
                    Constants.JPEG_CAPITAL
                )
            ) {
                o.inSampleSize = 6
            } else {
                o.inSampleSize = 6
            }

            var inputStream = FileInputStream(cameraFile)
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close()
            val REQUIRED_SIZE = 50
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(cameraFile)
            var selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)

            val ei: ExifInterface = ExifInterface(cameraFile!!.path)
            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> selectedBitmap = rotateImage(
                    selectedBitmap!!, 90F
                )
                ExifInterface.ORIENTATION_ROTATE_180 -> selectedBitmap = rotateImage(
                    selectedBitmap!!, 180F
                )
                ExifInterface.ORIENTATION_ROTATE_270 -> selectedBitmap = rotateImage(
                    selectedBitmap!!, 270F
                )
                ExifInterface.ORIENTATION_NORMAL -> {}
                else -> {}
            }
            inputStream.close()
            val folder = File("${context!!.getCacheDir()}/FolderName")
            var success: Boolean = true
            if (!folder.exists()) {
                success = folder.mkdir()
            }
            if (success) {
                val newFile: File = File(File(folder.getAbsolutePath()), cameraFile!!.name)
                if (newFile.exists()) {
                    newFile.delete();
                }
                val outputStream = FileOutputStream(newFile)

                if ((cameraFile!!.name).equals(Constants.JPG_SMALL) || (cameraFile!!.name).equals(
                        Constants.JPG_CAPITAL
                    ) || (cameraFile!!.name).equals(
                        Constants.PNG_SMALL
                    ) || (cameraFile!!.name).equals(Constants.PNG_CAPITAL) || (cameraFile!!.name).equals(
                        Constants.JPEG_SMALL
                    ) || (cameraFile!!.name).equals(
                        Constants.JPEG_CAPITAL
                    )
                ) {
                    selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                } else {
                    selectedBitmap!!.compress(Bitmap.CompressFormat.PNG, 30, outputStream);
                }
                return newFile
            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }

    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    fun convertUTCtoTime(
        createdAt: String,
        timePattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    ): String {
        val format = SimpleDateFormat(timePattern)
        format.timeZone = TimeZone.getTimeZone("GMT")
        val date = format.parse(createdAt)
        val createdTimeInMs = date?.time
        val currentTimeInMs = System.currentTimeMillis()
        val differenceTimeInMs = currentTimeInMs - createdTimeInMs.toString().toLong()
        val hours = TimeUnit.MILLISECONDS.toHours(differenceTimeInMs)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(differenceTimeInMs)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(differenceTimeInMs)
        val days = TimeUnit.MILLISECONDS.toDays(differenceTimeInMs)

        if (seconds < 60) {
            return "Just now"
        } else if (minutes < 60) {
            return minutes.toString() + "m"
        } else if (hours < 24) {
            return hours.toString() + "h"
        } else {
            return days.toString() + "d"
        }
    }

    fun conversionForTimer(hours: String, minutes: String, seconds: String): Long {
        val hoursInMillis =
            TimeUnit.HOURS.toMillis(hours.toLong())
        val minsInMillis =
            TimeUnit.MINUTES.toMillis(minutes.toLong())
        val secsInMillis =
            TimeUnit.SECONDS.toMillis(seconds.toLong())
        val totalTimeInMillis = hoursInMillis + minsInMillis + secsInMillis

        return totalTimeInMillis
    }

    fun convertToCurrencyFormat(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("en", "in"))
            .format(amount)
    }


    fun clickEventTrack(mag: String) {

    }

    fun currencyConversion(price: Double): String? {
        var value: String = ""
        if (price >= 0 && price < 100000) {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            value = "₹${df.format(price)} "
        } else if (price >= 100000 && price < 1000000) {
            val amount = (price / 100000)
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            value = "₹${df.format(amount)} Lakhs* "
        } else if (price >= 1000000 && price < 10000000) {
            val amount = price / 100000
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            value = "₹${df.format(amount)} Lakhs* "
        } else if (price >= 10000000 && price < 100000000) {
            val amount = price / 10000000
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            value = "₹${df.format(amount)} Cr "
        } else if (price >= 100000000 && price < 1000000000) {
            val amount = price / 10000000
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            value = "₹${df.format(amount)} Cr "
        }
        return value
    }

}



