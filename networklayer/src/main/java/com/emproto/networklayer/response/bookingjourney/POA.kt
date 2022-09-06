package com.emproto.networklayer.response.bookingjourney

import com.emproto.networklayer.response.profile.AccountsResponse

data class POA(
    val isPOAAlloted: Boolean,
    val isPOARequired: Boolean,
    val poaLetter: AccountsResponse.Data.Document?
)