package com.emproto.networklayer.response.investment

data class MediaGallery(
    val coverImage: List<CoverImage>,
    val createdAt: String,
    val droneShoots: List<DroneShoot>,
    val id: Int,
    val images: List<Image>,
    val isCoverImageActive: Boolean,
    val isDroneShootsActive: Boolean,
    val isImagesActive: Boolean,
    val isThreeSixtyImagesActive: Boolean,
    val isVideosActive: Boolean,
    val projectContentId: Int,
    val threeSixtyImages: List<ThreeSixtyImage>,
    val updatedAt: String,
    val videos: List<Video>
)