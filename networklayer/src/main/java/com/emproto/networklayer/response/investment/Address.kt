package com.emproto.networklayer.response.investment

import java.io.Serializable

data class Address(
    val city: String,
    val country: String,
    val gpsLocationLink: String,
    val mapMedia: MapMedia,
    val pinCode: String,
    val state: String,
    val isMapDetailsActive:Boolean = true
):Serializable