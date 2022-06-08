package com.emproto.networklayer.response.investment

data class Data(
    val page: Page,
    val pageManagementsOrNewInvestments: List<PageManagementsOrNewInvestment>,
    val pageManagementsOrCollectionOneModels: List<PageManagementsOrCollectionOneModel>,
    val pageManagementsOrCollectionTwoModels: List<PageManagementsOrCollectionTwoModel>
)