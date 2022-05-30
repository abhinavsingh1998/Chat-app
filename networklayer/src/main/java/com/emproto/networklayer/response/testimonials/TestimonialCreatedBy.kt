package com.emproto.networklayer.response.testimonials

data class TestimonialCreatedBy(
    val adminType: String,
    val countryCode: String,
    val createdAt: String,
    val createdBy: Any,
    val crmAdminId: Any,
    val department: Any,
    val designation: Any,
    val email: String,
    val firstName: String,
    val id: Int,
    val isResetPasswordCompleted: Boolean,
    val lastName: Any,
    val password: String,
    val phoneNumber: String,
    val roleId: Int,
    val status: String,
    val updatedAt: String,
    val updatedBy: Any
)