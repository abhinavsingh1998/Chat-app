package com.emproto.networklayer.response.fm

data class FmUploadResponse(
    val error: Boolean,
    val file_name: String,
    val file_url: String,
    val message: String
)