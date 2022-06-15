package com.emproto.networklayer.response.bookingjourney

import com.emproto.networklayer.response.profile.AccountsResponse

data class Allotment(
    val allotmentDate: String,
    val allotmentLetter: AccountsResponse.Data.Document
)