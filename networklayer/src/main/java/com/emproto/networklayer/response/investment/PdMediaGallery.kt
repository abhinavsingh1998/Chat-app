package com.emproto.networklayer.response.investment

data class PdMediaGallery(
    val coverImage: List<PdCoverImage>,
    val createdAt: String,
    val droneShoots: List<PdDroneShoot>,
    val id: Int,
    val images: List<PdImage>,
    val isCoverImageActive: Boolean,
    val isDroneShootsActive: Boolean,
    val isImagesActive: Boolean,
    val isThreeSixtyImagesActive: Boolean,
    val isVideosActive: Boolean,
    val projectContentId: Int,
    val threeSixtyImages: List<PdThreeSixtyImage>,
    val updatedAt: String,
    val videos: List<PdVideo>
)