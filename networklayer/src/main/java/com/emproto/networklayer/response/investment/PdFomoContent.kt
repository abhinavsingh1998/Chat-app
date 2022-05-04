package com.emproto.networklayer.response.investment

data class PdFomoContent(
    val days: Int,
    val isDaysActive: Boolean,
    val isNoOfViewsActive: Boolean,
    val isTargetTimeActive: Boolean,
    val noOfViews: Int,
    val targetTime: PdTargetTime
)