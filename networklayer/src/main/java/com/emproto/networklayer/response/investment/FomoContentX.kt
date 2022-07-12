package com.emproto.networklayer.response.investment

import java.io.Serializable

data class FomoContentX(
    val days: Int,
    val isDaysActive: Boolean,
    val isNoOfViewsActive: Boolean,
    val isTargetTimeActive: Boolean,
    val noOfViews: Int,
    val targetTime: TargetTime
): Serializable