package com.emproto.networklayer.response.bookingjourney

import com.emproto.networklayer.response.profile.AccountsResponse
import com.google.gson.annotations.SerializedName

data class Documents(
    @SerializedName("712")
    val SEVEN: AccountsResponse.Data.Document,
    val DOC: AccountsResponse.Data.Document
)