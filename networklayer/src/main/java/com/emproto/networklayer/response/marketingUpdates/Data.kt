package com.emproto.networklayer.response.marketingUpdates

data class Data(
    val createdAt: String,
    val createdBy: Int,
    val detailedInfo: List<DetailedInfo>,
    val displayTitle: String,
    val formType: String,
    val id: Int,
    val marketingUpdateCreatedBy: MarketingUpdateCreatedBy,
    val marketingUpdateUpdatedBy: MarketingUpdateUpdatedBy,
    val priority: Int,
    val shouldDisplayDate: Boolean,
    val status: String,
    val subTitle: String,
    val updateType: String,
    val updatedAt: String,
    val updatedBy: Int
)