package com.emproto.networklayer.response.investment

data class PageManagementOrLatestUpdate(
    val content: String,
    val createdAt: String,
    val createdBy: Int,
    val id: Int,
    val pageManagementAndLatestUpdates: PageManagementAndLatestUpdates,
    val projectContentId: Int,
    val status: String,
    val type: String,
    val updatedAt: String,
    val updatedBy: Any
)