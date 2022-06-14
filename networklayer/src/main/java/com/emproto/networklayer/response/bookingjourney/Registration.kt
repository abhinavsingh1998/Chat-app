package com.emproto.networklayer.response.bookingjourney

data class Registration(
    val isRegistrationScheduled: Boolean,
    val registrationDate: String,
    val registrationNumber: Any
)