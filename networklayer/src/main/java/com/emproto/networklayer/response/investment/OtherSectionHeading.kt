package com.emproto.networklayer.response.investment

data class OtherSectionHeading(
    val droneVideos: DroneVideos,
    val promises: Promises,
    val testimonials: Testimonials
)

data class DroneVideos(
    val sectionHeading: String
)

data class Promises(
    val sectionHeading: String
)

data class Testimonials(
    val sectionHeading: String
)