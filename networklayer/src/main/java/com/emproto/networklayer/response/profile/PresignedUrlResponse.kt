package com.emproto.networklayer.response.profile


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PresignedUrlResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("preSignedUrl")
    val preSignedUrl: String
):Serializable