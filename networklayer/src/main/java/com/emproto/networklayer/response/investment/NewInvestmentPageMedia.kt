package com.emproto.networklayer.response.investment

import java.io.Serializable

data class NewInvestmentPageMedia(
    val key: String,
    val name: String,
    val value: Value
): Serializable