package com.example.portfolioui.models

import android.widget.TextView
import com.emproto.networklayer.response.bookingjourney.Payment

data class BookingStepsModel(
    val type: Int,
    val text: String,
    val description: String,
    val linkText: String,
    val data: Any? = null
)
