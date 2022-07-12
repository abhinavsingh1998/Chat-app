package com.emproto.networklayer.response.investment

import java.io.Serializable

data class Point(
    val halfYear: String,
    val month: String,
    val quater: String,
    val value: Int,
    val year: String
): Serializable