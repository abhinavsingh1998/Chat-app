package com.emproto.networklayer.response.watchlist

data class FomoContent(
    val days: Int,
    val isDaysActive: Boolean,
    val isNoOfViewsActive: Boolean,
    val isTargetTimeActive: Boolean,
    val noOfViews: Int,
    val targetTime: TargetTime
)