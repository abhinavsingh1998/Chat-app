package com.emproto.networklayer.response.home

data class HomePagesOrPromise(
    val createdAt: String,
    val createdBy: Any,
    val crmPromiseId: String,
    val description: List<String>,
    val displayMedia: Any,
    val homePagesAndPromises: HomePagesAndPromises,
    val howToApply: HowToApply,
    val id: Int,
    val isHowToApplyActive: Boolean,
    val name: String,
    val promiseIconType: String,
    val promiseMedia: PromiseMedia,
    val promiseType: String,
    val shortDescription: String,
    val status: String,
    val tAndCUrlLink: String,
    val updatedAt: String,
    val updatedBy: Any
)