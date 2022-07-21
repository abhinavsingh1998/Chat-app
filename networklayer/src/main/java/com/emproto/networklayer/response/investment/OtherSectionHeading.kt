package com.emproto.networklayer.response.investment

data class OtherSectionHeading(
    val droneVideos: DroneVideos,
    val promises: Promises,
    val testimonials: Testimonials,
    val inventoryBucketContents: InventoryBucketContents,
    val faqSection: FaqSections
)

data class DroneVideos(
    val sectionHeading: String
)

data class Promises(
    val sectionHeading: String
)

data class Testimonials(
    val subHeading: String,
    val sectionHeading: String
)

data class InventoryBucketContents(
    val subHeading: String,
    val sectionHeading: String
)

data class FaqSections(
    val subHeading: String,
    val sectionHeading: String
)