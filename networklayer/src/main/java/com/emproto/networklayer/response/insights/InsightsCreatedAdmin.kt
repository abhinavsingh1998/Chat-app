package com.emproto.networklayer.response.insights

data class InsightsCreatedAdmin(
    val adminType: String,
    val countryCode: String,
    val createdAt: String,
    val createdBy: String,
    val crmAdminId: Int,
    val department: String,
    val designation: String,
    val email: String,
    val firstName: String,
    val id: Int,
    val isResetPasswordCompleted: Boolean,
    val lastName: String,
    val password: String,
    val phoneNumber: String,
    val roleId: Int,
    val status: String,
    val updatedAt: String,
    val updatedBy: Int
)