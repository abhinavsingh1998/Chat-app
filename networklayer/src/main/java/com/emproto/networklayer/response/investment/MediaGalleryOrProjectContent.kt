package com.emproto.networklayer.response.investment

data class MediaGalleryOrProjectContent(
    val coverImage: List<CoverImage>?=null,
    val createdAt: String?=null,
    val droneShoots: List<DroneShoot>?=null,
    val id: Int?=null,
    val images: List<Image>?=null,
    val isCoverImageActive: Boolean?=null,
    val isDroneShootsActive: Boolean?=null,
    val isImagesActive: Boolean?=null,
    val isThreeSixtyImagesActive: Boolean?=null,
    val isVideosActive: Boolean?=null,
    val latestMedaiGalleryProjectContentId: Any?=null,
    val medaiGalleryProjectContentId: Int?=null,
    val threeSixtyImages: List<ThreeSixtyImage>?=null,
    val updatedAt: String?=null,
    val videos: List<Video>?=null
)