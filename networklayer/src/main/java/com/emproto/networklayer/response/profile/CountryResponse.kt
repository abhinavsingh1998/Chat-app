package com.emproto.networklayer.response.profile

data class CountryResponse (
    val message : String,
    val code : Int,
    val data : List<Countries>

        )

data class Countries(
    val isoCode:String,
    val name:String
)
