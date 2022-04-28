package com.emproto.networklayer.response.home

data class PageManagementsOrTestimonial(
    val content: String,
    val createdAt: String,
    val createdBy: Int,
    val customerName: String,
    val id: Int,
    val mediaLink: String,
    val pageManagementAndTestimonials: PageManagementAndTestimonials,
    val projectContentId: Int,
    val status: String,
    val subTitle: String,
    val type: String,
    val updatedAt: String,
    val updatedBy: Any
)