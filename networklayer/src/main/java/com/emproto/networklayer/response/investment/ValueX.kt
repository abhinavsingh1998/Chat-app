package com.emproto.networklayer.response.investment

import java.io.Serializable

data class ValueX(
    val icon: Icon,
    val name: String,
    val points: List<String>
): Serializable