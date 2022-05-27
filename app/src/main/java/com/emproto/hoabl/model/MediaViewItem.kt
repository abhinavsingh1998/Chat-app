package com.emproto.hoabl.model

import java.io.Serializable

data class MediaViewItem(
    val mediaType: String,
    val media: String,
    val content: String? = ""
) : Serializable