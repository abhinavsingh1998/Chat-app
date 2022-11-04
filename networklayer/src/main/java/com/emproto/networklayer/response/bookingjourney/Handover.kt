package com.emproto.networklayer.response.bookingjourney

import com.emproto.networklayer.response.profile.AccountsResponse

data class Handover(
    val guidelines: AccountsResponse.Data.Document,
    val handoverDate: String,
    var handoverFlag: Boolean
)