package com.emproto.networklayer.response.home

data class PageManagementOrLatestUpdate(
    val createdAt: String,
    val createdBy: Int,
    val detailedInfo: Any,
    val displayTitle: Any,
    val formType: Any,
    val id: Int,
    val pageManagementAndLatestUpdates: PageManagementAndLatestUpdates,
    val shouldDisplayDate: Any,
    val subTitle: Any,
    val updateType: Any,
    val updatedAt: String,
    val updatedBy: Any
)