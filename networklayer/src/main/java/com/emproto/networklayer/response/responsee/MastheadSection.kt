package com.emproto.networklayer.response.responsee

data class MastheadSection(
    val grossWeightedAvgAppreciation: GrossWeightedAvgAppreciation,
    val totalAmoutOfLandTransacted: TotalAmoutOfLandTransacted,
    val totalNumberOfUsersWhoBoughtTheLand: TotalNumberOfUsersWhoBoughtTheLand,
    val totalSqftOfLandTransacted: TotalSqftOfLandTransacted
)