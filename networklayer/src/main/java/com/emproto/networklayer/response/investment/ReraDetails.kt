package com.emproto.networklayer.response.investment

import java.io.Serializable

data class ReraDetails(
    val companyNameAndAddress: String,
    val reraNumbers: List<String>,
    val isReraDetailsActive:Boolean
): Serializable