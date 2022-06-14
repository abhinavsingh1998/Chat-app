package com.emproto.hoabl.model

data class RecyclerViewItem(
    val viewType: Int,
    val data: Any? = null,
    val iea: String = "",
    val iid: Int = 0
)
