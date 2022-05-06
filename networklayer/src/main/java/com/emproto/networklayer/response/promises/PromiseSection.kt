package com.emproto.networklayer.response.promises

data class PromiseSection(
    val hoablPromiseDisplayName: String,
    val projectPromiseDisplayName: String,
    val sectionName: String,
    val subDescription: String,
    val disclaimer: String,
    val aboutPromises: AboutPromises
)

data class AboutPromises(val sectionHeading: String, val subDescription: String)