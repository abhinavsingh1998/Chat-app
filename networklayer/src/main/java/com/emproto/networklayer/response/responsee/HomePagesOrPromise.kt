package com.emproto.networklayer.response.responsee

data class HomePagesOrPromise(
    val createdAt: String,
    val createdBy: Any,
    val crmId: Any,
    val description: Any,
    val displayMedia: DisplayMedia,
    val howToApply: HowToApply,
    val id: Int,
    val isHowToApplyActive: Boolean,
    val isTermsAndConditionsActive: Boolean,
    val name: String,
    val priority: Int,
    val promiseIconType: String,
    val promiseMedia: Any,
    val promiseType: Any,
    val shortDescription: String,
    val status: String,
    val termsAndConditions: TermsAndConditions,
    val updatedAt: String,
    val updatedBy: Int
)