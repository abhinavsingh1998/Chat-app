package com.emproto.networklayer.response.portfolio.ivdetails

data class MediaGalleryOrProjectContent(
    val coverImage: List<CoverImageX>,
    val createdAt: String,
    val droneShoots: List<DroneShootX>,
    val id: Int,
    val images: List<ImageX>,
    val isCoverImageActive: Boolean,
    val isDroneShootsActive: Boolean,
    val isImagesActive: Boolean,
    val isThreeSixtyImagesActive: Boolean,
    val isVideosActive: Boolean,
    val latestMedaiGalleryProjectContentId: Any,
    val medaiGalleryProjectContentId: Int,
    val threeSixtyImages: List<ThreeSixtyImageX>,
    val updatedAt: String,
    val videos: List<VideoX>
)