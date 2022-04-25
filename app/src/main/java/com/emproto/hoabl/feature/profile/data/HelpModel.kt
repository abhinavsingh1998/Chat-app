package com.emproto.hoabl.feature.profile.data

import com.emproto.hoabl.feature.promises.data.DataModel

data class HelpModel(
    val viewType: Int,
    var title: String,
    var desc: String,
    var image: Int,
    var arrowImage: Int,
    val data: List<DataHealthCenter>
)
