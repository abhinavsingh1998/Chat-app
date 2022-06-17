package com.emproto.networklayer.response.investment

import java.io.Serializable

data class ProjectCoverImages(
    val chatListViewPageMedia: ChatListViewPageMedia,
    val chatPageMedia: ChatPageMedia,
    val collectionListViewPageMedia: CollectionListViewPageMedia,
    val homePageMedia: HomePageMedia,
    val newInvestmentPageMedia: NewInvestmentPageMedia,
    val portfolioPageMedia: PortfolioPageMedia
) : Serializable