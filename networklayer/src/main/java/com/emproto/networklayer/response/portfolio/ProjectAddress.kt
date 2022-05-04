package com.emproto.networklayer.response.portfolio

data class ProjectAddress(
    val city: String,
    val country: String,
    val gpsLocationLink: String,
    val pinCode: String,
    val state: String
)