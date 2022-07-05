package com.emproto.networklayer.response.home

import java.io.Serializable

data class HomeResponse(
    val code: Int,
    val `data`: Data

):Serializable