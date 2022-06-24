package com.emproto.networklayer.response.investment

data class Faq(
    val categoryId: Int,
    val createdAt: String,
    val createdBy: Int,
    val faqAnswer: FaqAnswer,
    val faqQuestion: FaqQuestion,
    val id: Int,
    val priority: Int,
    val projectContentId: Int,
    val status: Boolean,
    val typeOfFAQ: String,
    val updatedAt: String,
    val updatedBy: String
)