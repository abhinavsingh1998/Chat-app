package com.emproto.hoabl.feature.promises.data

import com.emproto.networklayer.response.promises.HomePagesOrPromise
import com.emproto.networklayer.response.promises.PromiseSection

data class PromisesData(
    val viewTyppe: Int,
    val image: String,
    val headerData: PromiseSection?,
    val data: List<HomePagesOrPromise>
)
