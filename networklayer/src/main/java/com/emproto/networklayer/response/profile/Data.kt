package com.emproto.networklayer.response.profile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Data(
    @SerializedName("city")
    val city: String,
    @SerializedName("contactType")
    val contactType: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("crmId")
    val crmId: String,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String,
    @SerializedName("deviceToken")
    val deviceToken: Any?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("houseNumber")
    val houseNumber: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isFacilityVisible")
    val isFacilityVisible: Boolean,
    @SerializedName("isKycComplete")
    val isKycComplete: Boolean,
    @SerializedName("isProfileComplete")
    val isProfileComplete: Boolean,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("locality")
    val locality: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("pincode")
    val pincode: Any?,
    @SerializedName("profilePictureUrl")
    val profilePictureUrl: String?,
    @SerializedName("state")
    val state: String,
    @SerializedName("streetAddress")
    val streetAddress: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("whatsappConsent")
    val whatsappConsent: Boolean,
    @SerializedName("showPushNotifications")
    val showPushNotifications: Boolean,
    @SerializedName("pageManagement")
    val pageManagement: PageManagement
) : Serializable
