package com.emproto.networklayer.response.portfolio.ivdetails

data class LatestMediaGalleryOrProjectContent(
    val coverImage: List<Image>,
    val createdAt: String,
    val droneShoots: List<Image>,
    val id: Int,
    val images: List<Image>,
    val isCoverImageActive: Boolean,
    val isDroneShootsActive: Boolean,
    val isImagesActive: Boolean,
    val isThreeSixtyImagesActive: Boolean,
    val isVideosActive: Boolean,
    val latestMedaiGalleryProjectCon: Int,
    val medaiGalleryProjectContentId: Any,
    val threeSixtyImages: List<Image>,
    val updatedAt: String,
    val videos: List<Image>
)