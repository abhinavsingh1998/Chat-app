package com.emproto.networklayer.response.profile
import com.google.gson.annotations.SerializedName


data class UploadKycResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("response")
        val response: Response
    ) {
        data class Response(
            @SerializedName("data")
            val `data`: AccountsResponse.Data.Document,
            @SerializedName("isUpdated")
            val isUpdated: Boolean
        ) {

        }
    }
}