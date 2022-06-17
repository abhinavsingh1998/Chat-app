package com.emproto.networklayer.response.investment

data class HomePagesOrPromise(
    val createdAt: String,
    val createdBy: Any,
    val crmPromiseId: String,
    val description: List<String>,
    val homePagesAndPromises: HomePagesAndPromises,
    val howToApply: Any,
    val id: Int,
    val isHowToApplyActive: Any,
    val mediaLink: String,
    val name: String,
    val promiseType: String,
    val shortDescription: String,
    val status: String,
    val tAndCUrlLink: String,
    val updatedAt: String,
    val updatedBy: Any
)