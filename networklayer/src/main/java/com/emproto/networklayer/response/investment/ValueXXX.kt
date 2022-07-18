package com.emproto.networklayer.response.investment

import java.io.Serializable

data class ValueXXX(
    val gpsLink: String,
    val icon: IconX,
    val name: String,
    val latitude:Double,
    val longitude:Double,
    val hours:String,
    val minutes:String
): Serializable