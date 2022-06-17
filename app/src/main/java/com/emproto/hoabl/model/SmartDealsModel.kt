package com.emproto.hoabl.model

import com.emproto.networklayer.response.investment.PageManagementsOrCollectionOneModel

data class SmartDealsModel(
    val list :List<PageManagementsOrCollectionOneModel>,
    val isClicked:Boolean
)
