package com.example.portfolioui.models
data class BookingStepsModel(
    val type: Int?,
    val text: String?,
    val description: String?,
    val linkText: String?,
    val data: Any? = null,
    val disableLink: Boolean = false
)
