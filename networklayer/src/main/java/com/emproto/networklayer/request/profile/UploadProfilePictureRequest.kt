package com.emproto.networklayer.request.login.profile

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.Part
import java.io.File
import java.io.Serializable


data class UploadProfilePictureRequest(
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("file")
    val file: MultipartBody.Part
)
