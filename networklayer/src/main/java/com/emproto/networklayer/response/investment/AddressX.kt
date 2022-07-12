package com.emproto.networklayer.response.investment

import java.io.Serializable

data class AddressX(
    val city: String,
    val country: String,
    val gpsLocationLink: String,
    val mapMedia: MapMedia,
    val pinCode: String,
    val state: String
): Serializable