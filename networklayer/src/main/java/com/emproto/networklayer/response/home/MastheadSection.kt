package com.emproto.networklayer.response.home

import java.io.Serializable

data class MastheadSection(
    val grossWeightedAvgAppreciation: GrossWeightedAvgAppreciation,
    val totalAmoutOfLandTransacted: TotalAmoutOfLandTransacted,
    val totalNumberOfUsersWhoBoughtTheLand: TotalNumberOfUsersWhoBoughtTheLand,
    val totalSqftOfLandTransacted: TotalSqftOfLandTransacted
):Serializable