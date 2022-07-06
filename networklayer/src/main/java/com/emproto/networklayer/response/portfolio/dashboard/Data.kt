package com.emproto.networklayer.response.portfolio.dashboard

import com.emproto.networklayer.response.investment.PromotionAndOffersMedia
import com.emproto.networklayer.response.watchlist.Data

data class Data(
    val projects: List<Project>,
    val summary: Summary,
    var watchlist: List<Data>,
    val pageManagement: PageData
)

data class PageData(val code: Int, val data: DataA)
data class DataA(val page: PageA)
data class PageA(
    val pageName: String,
    val subHeading: String,
    val promotionAndOffersProjectContentId: Int,
    val isPromotionAndOfferActive: Boolean,
    val promotionAndOffersMedia: PromotionAndOffersMedia
)