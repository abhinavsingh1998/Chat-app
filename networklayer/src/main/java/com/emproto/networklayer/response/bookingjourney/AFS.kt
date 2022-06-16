package com.emproto.networklayer.response.bookingjourney

import com.emproto.networklayer.response.profile.AccountsResponse

data class AFS(
    val afsLetter: AccountsResponse.Data.Document,
    val isAfsVisible: Boolean
)