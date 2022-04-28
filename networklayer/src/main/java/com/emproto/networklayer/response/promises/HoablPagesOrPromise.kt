package com.emproto.networklayer.response.promises

data class HoablPagesOrPromise(
    val createdAt: String,
    val createdBy: Any,
    val crmPromiseId: String,
    val description: List<String>,
    val howToApply: HowToApply,
    val id: Int,
    val isHowToApplyActive: Boolean,
    val mediaLink: String,
    val name: String,
    val promiseType: String,
    val shortDescription: String,
    val status: String,
    val tAndCUrlLink: String,
    val updatedAt: String,
    val updatedBy: Any,
    val promiseMedia:PromisesMedia
)