package com.emproto.hoabl.model

import com.emproto.networklayer.response.investment.PageManagementsOrCollectionTwoModel

data class TrendingModel(
    val list :List<PageManagementsOrCollectionTwoModel>,
    val isClicked:Boolean
)
