package com.emproto.networklayer.response.home

import java.io.Serializable

data class Point(
    val halfYear: Any,
    val month: Any,
    val quater: Any,
    val value: Int,
    val year: String
):Serializable