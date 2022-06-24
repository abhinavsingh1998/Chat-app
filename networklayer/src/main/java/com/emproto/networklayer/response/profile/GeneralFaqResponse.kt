package com.emproto.networklayer.response.profile

data class GeneralFaqResponse(
    val code: Int,
    val `data`: List<GfData>,
    val message: String,
    val pageIndex: Any,
    val pageSize: Any,
    val totalCount: Int,
    val totalPages: Any
)

data class GfData(
    val category: Category,
    val categoryId: Int,
    val createdAt: String,
    val createdBy: Int,
    val faqAnswer: FaqAnswer,
    val faqQuestion: FaqQuestion,
    val faqsCreatedAdmin: FaqsCreatedAdmin,
    val faqsUpdatedAdmin: Any,
    val id: Int,
    val priority: Int,
    val projectContentId: Int,
    val status: Boolean,
    val typeOfFAQ: String,
    val updatedAt: String,
    val updatedBy: Any
)

data class Category(
    val categoryType: String,
    val createdAt: String,
    val id: Int,
    val name: String,
    val numberOfFaqs: Int,
    val priority: Int,
    val status: String,
    val updatedAt: String
)

data class FaqAnswer(
    val answer: String
)

data class FaqQuestion(
    val question: String
)

data class FaqsCreatedAdmin(
    val firstName: String,
    val id: Int
)