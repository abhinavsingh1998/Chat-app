package com.emproto.networklayer.response.investment

data class Address(
    val city: String,
    val country: String,
    val gpsLocationLink: String,
    val pinCode: String,
    val state: String
)