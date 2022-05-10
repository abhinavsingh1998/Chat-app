package com.emproto.networklayer.response.portfolio.ivdetails

data class FrequentlyAskedQuestion(
    val categoryId: Int,
    val createdAt: String,
    val createdBy: Int,
    val faqAnswer: FaqAnswer,
    val faqQuestion: FaqQuestion,
    val id: Int,
    val priority: Any,
    val projectContentId: Int,
    val status: Boolean,
    val typeOfFAQ: String,
    val updatedAt: String,
    val updatedBy: Any
)