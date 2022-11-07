package com.emproto.networklayer.response.portfolio.dashboard


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CustomerGuideLines(
        @SerializedName("key")
        val key: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: Value?
    ):Serializable
{
        data class Value(
            @SerializedName("url")
            val url: String?
        ):Serializable
    }