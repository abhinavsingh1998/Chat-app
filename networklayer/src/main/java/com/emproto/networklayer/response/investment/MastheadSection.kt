package com.emproto.networklayer.response.investment

data class MastheadSection(
    val grossWeightedAvgAppreciation: GrossWeightedAvgAppreciation,
    val totalAmoutOfLandTransacted: TotalAmoutOfLandTransacted,
    val totalNumberOfUsersWhoBoughtTheLand: TotalNumberOfUsersWhoBoughtTheLand,
    val totalSqftOfLandTransacted: TotalSqftOfLandTransacted
)