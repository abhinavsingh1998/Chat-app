package com.emproto.networklayer.response.home

data class MastheadSection(
    val grossWeightedAvgAppreciation: GrossWeightedAvgAppreciation,
    val totalAmoutOfLandTransacted: TotalAmoutOfLandTransacted,
    val totalNumberOfUsersWhoBoughtTheLand: TotalNumberOfUsersWhoBoughtTheLand,
    val totalSqftOfLandTransacted: TotalSqftOfLandTransacted
)