package com.emproto.networklayer.response.promises

import com.emproto.networklayer.response.home.DisplayMedia

data class HomePagesOrPromise(
    val createdAt: String,
    val createdBy: String,
    val crmPromiseId: String,
    val description: String,
    val displayMedia: DisplayMedia,
    val howToApply: HowToApply,
    val id: Int,
    val isHowToApplyActive: Boolean,
    val isTermsAndConditionsActive: Boolean,
    val name: String,
    val priority: Any,
    val promiseIconType: String,
    val promiseMedia: PromiseMedia?,
    val promiseType: String,
    val shortDescription: String,
    val status: String,
    val termsAndConditions: TermsAndConditions,
    val updatedAt: String,
    val updatedBy: Any
)