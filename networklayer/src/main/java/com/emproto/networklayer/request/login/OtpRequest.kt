package com.emproto.networklayer.request.login

data class OtpRequest(val phoneNumber: String, val countryCode: String, val country: String)