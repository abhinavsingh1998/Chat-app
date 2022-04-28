package com.emproto.networklayer.response.home

data class PageManagementsOrReferral(
    val createdAt: String,
    val createdBy: Int,
    val displayTitle: String,
    val id: Int,
    val mediaLink: String,
    val pageManagementAndReferrals: PageManagementAndReferrals,
    val status: String,
    val subTitle: String,
    val updatedAt: String,
    val updatedBy: Any
)