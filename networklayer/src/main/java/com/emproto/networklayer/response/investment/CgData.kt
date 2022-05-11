package com.emproto.networklayer.response.investment

data class CgData(
    val faqs: List<Faq>,
    val id: Int,
    val name: String,
    val numberOfFaqs: Int,
    val priority: Int,
    val status: String
)