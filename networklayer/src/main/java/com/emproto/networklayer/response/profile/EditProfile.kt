package com.emproto.networklayer.response.profile

data class EditProfile(
    val firstName: String,
    val lastName :String,
    val email: String,
    val dateOfBirth: Int,
    val gender:String,
    val houseNumber:String,
    val streetAddress:String,
    val locality:String,
    val pincode:Int,
    val city:String,
    val state:String,
    val country:String
    )

