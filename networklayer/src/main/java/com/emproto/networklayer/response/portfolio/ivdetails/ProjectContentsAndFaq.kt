package com.emproto.networklayer.response.portfolio.ivdetails

data class ProjectContentsAndFaq(
    val createdAt: String,
    val faqId: Int,
    val frequentlyAskedQuestion: FrequentlyAskedQuestion,
    val id: Int,
    val priority: Int,
    val projectContentId: Int,
    val updatedAt: String
)