package com.emproto.networklayer.response.actionItem

data class Data(
    val actionItemType: Int,
    val address: Address,
    val bookingStatus: Int,
    val cardTitle: String,
    val displayText: String,
    val displayTitle: String,
    val inventoryId: String,
    val investmentId: Int,
    val launchName: String,
    val primaryOwner: String,
    val scheduleId: Int
)