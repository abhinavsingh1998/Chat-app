package com.emproto.networklayer.response.portfolio.ivdetails

data class ProjectInformation(
    val id: Int,
    val latestMediaGalleryOrProjectContent: List<LatestMediaGalleryOrProjectContent>,
    val launchName: String,
    val mediaGalleryOrProjectContent: List<MediaGalleryOrProjectContent>,
    val projectContentsAndFaqs: List<ProjectContentsAndFaq>,
    val reraDetails: ReraDetails,
    val shortDescription: String,
    val similarInvestments: List<SimilarInvestment>
)