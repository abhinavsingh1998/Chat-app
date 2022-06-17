package com.emproto.networklayer.response.watchlist

import java.io.Serializable

data class Address(
    val city: String,
    val country: String,
    val state: String
):Serializable