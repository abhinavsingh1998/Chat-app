package com.emproto.networklayer.response.responsee

data class PageManagementOrLatestUpdate(
    val createdAt: String,
    val createdBy: Int,
    val detailedInfo: List<DetailedInfo>,
    val displayTitle: String,
    val formType: String,
    val id: Int,
    val priority: Int,
    val shouldDisplayDate: Boolean,
    val status: String,
    val subTitle: String,
    val updateType: String,
    val updatedAt: String,
    val updatedBy: Int
)