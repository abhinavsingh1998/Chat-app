package com.emproto.networklayer.response.home

import java.io.Serializable

data class TermsAndConditions(
    val description: String,
    val displayName: String
):Serializable