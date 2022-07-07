package com.emproto.networklayer.response.profile

data class StatesResponse(
    val message : String,
    val code : Int,
    val data : List<States>

)

data class States(
    val isoCode:String,
    val name:String
)
data class Cities(
    val isoCode:String,
    val name:String
)