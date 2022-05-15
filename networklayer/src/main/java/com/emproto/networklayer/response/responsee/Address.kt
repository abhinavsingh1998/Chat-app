package com.emproto.networklayer.response.responsee

data class Address(
    val city: String,
    val country: String,
    val gpsLocationLink: String,
    val pinCode: String,
    val state: String
)