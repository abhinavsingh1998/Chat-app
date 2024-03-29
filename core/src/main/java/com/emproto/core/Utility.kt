package com.emproto.core

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import android.os.Environment
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.Log
import android.view.Window
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import java.io.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object Utility {

    /**
     * parsing local json file
     *
     * @param context
     * @param fileName
     * @return
     */
    public fun loadJSONFromAsset(context: Context, fileName: String): String {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
    }

    fun dateInWords(time: String): String? {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val inputFormat = SimpleDateFormat(inputPattern)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
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
            outputFormat.timeZone = TimeZone.getDefault()

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
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat(outputPattern)
        outputFormat.timeZone = TimeZone.getDefault()
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

    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
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
        outputFormat.timeZone = TimeZone.getDefault()
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
        inputFormat.timeZone
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


    fun writeResponseBodyToDisk(body: String, pdfName: String): File? {
        return try {
            // todo change the file location/name according to your needs
            val bytes = Base64.decode(body, Base64.DEFAULT)
            val fileSuffix = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
            val futureStudioIconFile =
                File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    pdfName + ".pdf"
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
        df.roundingMode = RoundingMode.HALF_UP
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
        df.roundingMode = RoundingMode.HALF_UP
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

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
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

        return if (seconds < 60) {
            "Just now"
        } else if (minutes < 60) {
            minutes.toString() + "m"
        } else if (hours < 24) {
            hours.toString() + "h"
        } else {
            days.toString() + "d"
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

    fun currencyConversion(price: Double): String? {
        var value: String = ""
        if (price >= 0 && price < 100000) {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP
            value = "₹${df.format(price)} "
        } else if (price >= 100000 && price < 1000000) {
            val amount = (price / 100000)
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP
            value = "₹${df.format(amount)} L "
        } else if (price >= 1000000 && price < 10000000) {
            val amount = price / 100000
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP
            value = "₹${df.format(amount)} L "
        } else if (price >= 10000000 && price < 100000000) {
            val amount = price / 10000000
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP
            value = "₹${df.format(amount)} Cr "
        } else if (price >= 100000000 && price < 1000000000) {
            val amount = price / 10000000
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP
            value = "₹${df.format(amount)} Cr "
        }
        return value
    }

    fun homeCurrencyConversion(price: Double): String? {
        var value: String = ""
        if (price >= 0 && price < 100000) {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP
            value = "₹${df.format(price)} "
        } else if (price >= 100000 && price < 1000000) {
            val amount = (price / 100000)
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP
            value = "₹${df.format(amount)} Lakhs* "
        } else if (price >= 1000000 && price < 10000000) {
            val amount = price / 100000
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP
            value = "₹${df.format(amount)} Lakhs* "
        } else if (price >= 10000000 && price < 100000000) {
            val amount = price / 10000000
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP
            value = "₹${df.format(amount)} Crore "
        } else if (price >= 100000000 && price < 1000000000) {
            val amount = price / 10000000
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP
            value = "₹${df.format(amount)} Crore "
        }
        return value
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    fun getDirectionURL(origin: LatLng, dest: LatLng, secret: String): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


}



