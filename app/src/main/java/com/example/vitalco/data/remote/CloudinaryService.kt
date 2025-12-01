package com.example.vitalco.data.remote

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface CloudinaryService {
    @Multipart
    @POST("image/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: okhttp3.RequestBody
    ): Response<CloudinaryResponse>
}

data class CloudinaryResponse(
    val secure_url: String
)
