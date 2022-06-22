package com.emproto.networklayer.response.resourceManagment

data class Data(
    val isFacilityVisible: Boolean,
    val isKycComplete: Boolean,
    val isProfileComplete: Boolean,
    val page: Page,
    val pageManagementsOrNewInvestments: List<Any>
)