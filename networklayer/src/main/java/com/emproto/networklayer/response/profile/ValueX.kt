package com.emproto.networklayer.response.profile

import java.io.Serializable

data class ValueX(
    val icon: Icon,
    val name: String,
    val points: List<String>
):Serializable