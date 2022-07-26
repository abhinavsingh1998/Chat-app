package com.emproto.networklayer.response.investment

class PageManagementContent : ArrayList<PageManagementContentItem>()

data class PageManagementContentItem(
    val facilityManagement: FacilityManagement,
    val totalPromisesOnHomeScreen: Int,
    val totalTestimonialsOnHomeScreen: Int
)

data class FacilityManagement(
    val key: String,
    val name: String,
    val value: ValueE
)

data class ValueE(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)