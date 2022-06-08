package com.emproto.networklayer.response.home

import com.emproto.networklayer.response.promises.DisplayMedia
import com.emproto.networklayer.response.promises.HowToApply
import com.emproto.networklayer.response.promises.PromiseMedia
import com.emproto.networklayer.response.promises.TermsAndConditions
import java.io.Serializable

data class HomePagesOrPromise(
    val createdAt: String,
    val createdBy: String?,
    val crmPromiseId: String?,
    val description: List<String>,
    val displayMedia: DisplayMedia,
    val howToApply: HowToApply,
    val id: Int,
    val isHowToApplyActive: Boolean,
    val isTermsAndConditionsActive: Boolean,
    val name: String,
    val priority: String?,
    val promiseIconType: String?,
    val promiseMedia: PromiseMedia?,
    val promiseType: String?,
    val shortDescription: String?,
    val status: String,
    val termsAndConditions: TermsAndConditions,
    val updatedAt: String,
    val updatedBy: String
):Serializable