package com.emproto.networklayer.response.home

import com.emproto.networklayer.response.responsee.DetailedInfo
import com.emproto.networklayer.response.responsee.PageManagementAndLatestUpdates

data class PageManagementOrLatestUpdate(
    val createdAt: String,
    val createdBy: Int,
    val detailedInfo: List<DetailedInfo>,
    val displayTitle: String,
    val formType: String,
    val id: Int,
    val pageManagementAndLatestUpdates: PageManagementAndLatestUpdates,
    val shouldDisplayDate: Boolean,
    val status: Any,
    val subTitle: String,
    val updateType: String,
    val updatedAt: String,
    val updatedBy: Int
)