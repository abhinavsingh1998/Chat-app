package com.emproto.hoabl.model

data class RecyclerViewItem(
    val viewType: Int,
    val data: Any? = null,
    val iea: String = "",
    val ea: Double = 0.0,
    val iid: Int = 0,
    val count: Int = 0
)
