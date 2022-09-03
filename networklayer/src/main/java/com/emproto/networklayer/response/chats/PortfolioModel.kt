package com.emproto.networklayer.response.chats

import java.io.Serializable

data class PortfolioModel(
    val bookingStatus: Int,
    val inventoryId: String,
    val investmentId: Int,
    val primaryOwner: String
):Serializable