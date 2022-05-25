package com.emproto.hoabl.model

import java.io.Serializable

data class MapLocationModel(
    val originLatitude:Double?,
    val originLongitude:Double?,
    val destinationLatitude:Double?,
    val destinationLongitude:Double?
):Serializable
