package com.emproto.networklayer.response.home

import java.io.Serializable

data class MastheadSection(
    val grossWeightedAvgAppreciation: GrossWeightedAvgAppreciation,
    val totalAmountOfLandTransacted: TotalAmoutOfLandTransacted,
    val totalNumberOfUsersWhoBoughtTheLand: TotalNumberOfUsersWhoBoughtTheLand,
    val totalSqftOfLandTransacted: TotalSqftOfLandTransacted
):Serializable