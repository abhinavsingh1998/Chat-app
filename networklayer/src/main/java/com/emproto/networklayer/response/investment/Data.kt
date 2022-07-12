package com.emproto.networklayer.response.investment

import java.io.Serializable

data class Data(
    val page: Page,
    val pageManagementsOrNewInvestments: List<PageManagementsOrNewInvestment>,
    val pageManagementsOrCollectionOneModels: List<PageManagementsOrCollectionOneModel>,
    val pageManagementsOrCollectionTwoModels: List<PageManagementsOrCollectionTwoModel>
):Serializable