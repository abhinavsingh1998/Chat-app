package com.emproto.networklayer.profile

data class EditUserNameRequest(
    val firstName : String,
    val lastName: String,
    val email: String,
    val dateOfBirth:String,
    val gender:String,
    val housenumber:String,
    val streetAddress:String,
    val locality:String,
    val pincode:String,
    val city:String,
    val state:String,
    val country:String
    )

