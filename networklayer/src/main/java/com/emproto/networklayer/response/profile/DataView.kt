package com.emproto.networklayer.response.profile

data class DataView(
    var id: String,
    var crmId : String,
    var phoneNumber: String,
    var firstName: String,
    var lastName:String,
    var profilePictureUrl: String,
    var otpVerified: String,
    var whatsappConsent: String,
    var contactType: String,
    var email: String,
    var countryCode:String,
    var status: String
)
