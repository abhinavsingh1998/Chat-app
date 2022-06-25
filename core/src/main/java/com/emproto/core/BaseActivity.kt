package com.emproto.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar


abstract class BaseActivity : AppCompatActivity() {

    var view: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showError(message: String?, view: View?) {
        Snackbar.make(view!!, message!!, Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackMessage(message: String?, view: View?) {
        Snackbar.make(view!!, message!!, Snackbar.LENGTH_SHORT).show()
    }

    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            HtmlCompat.fromHtml(message!!, HtmlCompat.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }

    /*override fun onNetworkChange(isConnected: Boolean) {
        if (!isConnected) {
            showSnackBar(view)
        }
    }
*/

    open fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork // network is currently in a high power state for performing data transmission.
            Log.d("Network", "active network $network")
            network ?: return false // return false if network is null
            val actNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false // return false if Network Capabilities is null
            return when {
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> { // check if wifi is connected
                    Log.d("Network", "wifi connected")
                    true
                }
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> { // check if mobile dats is connected
                    Log.d("Network", "cellular network connected")
                    true
                }
                else -> {
                    Log.d("Network", "internet not connected")
                    false
                }
            }
        }
        return false
    }

    fun showErrorView(view: View?, text: String) {
        val snackbar = Snackbar.make(view!!, "", Snackbar.LENGTH_LONG)
        val customSnackView: View = layoutInflater.inflate(R.layout.error_layout, null)
        val message = customSnackView.findViewById<TextView>(R.id.error_text)
        message.text = text
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        // add the custom snack bar layout to snackbar layout
        snackbarLayout.addView(customSnackView, 0)

        snackbar.show()
    }
    fun showSuccessView(view: View?, text: String) {
        val snackbar = Snackbar.make(view!!, "", Snackbar.LENGTH_LONG)
        val customSnackView: View = layoutInflater.inflate(R.layout.error_layout, null)
        val message = customSnackView.findViewById<TextView>(R.id.error_text)
        val viewColor= customSnackView.findViewById<View>(R.id.view)
        viewColor.setBackgroundColor(resources.getColor(R.color.green))
        message.text = text
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        // add the custom snack bar layout to snackbar layout
        snackbarLayout.addView(customSnackView, 0)

        snackbar.show()
    }



    fun showSnackBar(view: View?) {
        val snackbar = Snackbar.make(view!!, "", Snackbar.LENGTH_LONG)
        val customSnackView: View = layoutInflater.inflate(R.layout.custom_snackbar_view, null)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 5, 0, 5)
        val imageView: ImageView = customSnackView.findViewById(R.id.refresh)
/*
        imageView.setOnClickListener {
            val connectivity = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = connectivity.activeNetworkInfo
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    Toast.makeText(applicationContext, activeNetwork.typeName, Toast.LENGTH_SHORT)
                        .show()
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    Toast.makeText(applicationContext, activeNetwork.typeName, Toast.LENGTH_SHORT)
                        .show()
                }
                snackbar.dismiss()
            } else {
                snackbar.dismiss()
                // not connected to the internet
                showError(resources.getString(R.string.NO_INTERNET), view)
            }
            //snackbar.dismiss()
        }*/

        // add the custom snack bar layout to snackbar layout
        snackbarLayout.addView(customSnackView, 0)

        snackbar.show()
    }

    open fun isValidPhoneNumber(phoneNumber: CharSequence?): Boolean {
        return if (!TextUtils.isEmpty(phoneNumber)) {
            Patterns.PHONE.matcher(phoneNumber).matches()
        } else false
    }


    open fun hideSoftKeyboard() {
        try {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isAcceptingText) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } else {
            }
        } catch (e: Exception) {
        }
    }

    fun View.hide() {
        this.visibility = View.GONE
    }

    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.invisible() {
        this.visibility = View.INVISIBLE
    }

}