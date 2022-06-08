package com.emproto.networklayer.response.profile

import java.io.Serializable

class Data (
    val contactType: String,
    val countryCode: String,
    val crmId: Int,
    val dateOfBirth: String,
    val email: String,
    val firstName: String,
    val gender: Int,
    val id: Int,
    val lastName: String,
    val otpVerified: Boolean,
    val phoneNumber: String,
    val profilePictureUrl: String,
    val status: String,
    val whatsappConsent: Boolean,
    val city:String,
    val state:String,
    val pincode:Int,
    val locality:String,
    val houseNumber:String,
    val streetAddress:String) : Serializable
