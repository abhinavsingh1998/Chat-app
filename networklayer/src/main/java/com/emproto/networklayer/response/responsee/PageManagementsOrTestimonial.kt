package com.emproto.networklayer.response.responsee

data class PageManagementsOrTestimonial(
    val companyName: String,
    val createdAt: String,
    val createdBy: Int,
    val designation: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val pageManagementAndTestimonials: PageManagementAndTestimonials,
    val priority: Int,
    val status: String,
    val testimonialContent: String,
    val updatedAt: String,
    val updatedBy: Any
)