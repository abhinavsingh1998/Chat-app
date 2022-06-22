package com.emproto.networklayer.response.documents

data class Data(
    val documentCategory: String,
    val documentType: String,
    val id: Int,
    val link: String,
    val name: String,
    val projectId: String,
    val userId: String,
    val path:String
)