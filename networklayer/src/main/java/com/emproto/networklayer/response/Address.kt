package com.emproto.networklayer.response

data class Address(
    val city: String,
    val country: String,
    val gpsLocationLink: String,
    val mapMedia: MapMedia,
    val pinCode: String,
    val state: String
)