package com.emproto.networklayer.response.investment

import java.io.Serializable

data class NewInvestments(
    val displayName: String,
    val subHeading: String
): Serializable