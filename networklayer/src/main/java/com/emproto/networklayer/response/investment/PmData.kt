package com.emproto.networklayer.response.investment

import com.emproto.networklayer.response.promises.DisplayMedia
import com.emproto.networklayer.response.promises.HowToApply
import com.emproto.networklayer.response.promises.PromiseMedia
import com.emproto.networklayer.response.promises.TermsAndConditions

data class PmData(
    val createdAt: String?=null,
    val createdBy: String?=null,
    val crmPromiseId: String?=null,
    val description: List<String>?=null,
    val displayMedia: DisplayMedia?=null,
    val howToApply: HowToApply?=null,
    val id: Int?=null,
    val isHowToApplyActive: Boolean?=null,
    val isTermsAndConditionsActive: Boolean?=null,
    val name: String?=null,
    val priority: String?=null,
    val promiseIconType: String?=null,
    val promiseMedia: PromiseMedia?=null,
    val promiseType: String?=null,
    val shortDescription: String?=null,
    val status: String?=null,
    val termsAndConditions: TermsAndConditions?=null,
    val updatedAt: String?=null,
    val updatedBy: String?=null
)