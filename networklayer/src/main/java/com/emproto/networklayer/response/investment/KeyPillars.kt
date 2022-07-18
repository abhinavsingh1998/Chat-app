package com.emproto.networklayer.response.investment

import java.io.Serializable

data class KeyPillars(
    val heading: String,
    val values: List<ValueX>
): Serializable