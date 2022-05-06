package com.emproto.networklayer.response.documents

data class DocumentsResponse(
    val code: Int,
    val `data`: List<Data>,
    val message: String
)