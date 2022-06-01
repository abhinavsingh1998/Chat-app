package com.emproto.networklayer.response.home

import com.emproto.networklayer.response.marketingUpdates.DetailedInfo
import java.io.Serializable

data class PageManagementOrLatestUpdate(
    val createdAt: String,
    val createdBy: Int,
    val detailedInfo: List<DetailedInfo>,
    val displayTitle: String,
    val formType: String,
    val id: Int,
    val shouldDisplayDate: Boolean,
    val status: String,
    val subTitle: String,
    val updateType: String,
    val updatedAt: String,
    val updatedBy: Int
):Serializable