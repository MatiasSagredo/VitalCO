package com.example.vitalco.data.remote

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object CloudinaryManager {
    private const val CLOUD_NAME = "dkhjnwrvi"
    private const val UPLOAD_PRESET = "vitalco_preset" // Reemplazar con tu upload preset real (unsigned)

    private val service: CloudinaryService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.cloudinary.com/v1_1/$CLOUD_NAME/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudinaryService::class.java)
    }

    suspend fun uploadImage(filePath: String): String? {
        return try {
            val file = File(filePath)
            if (!file.exists()) return null

            val mediaType = MediaType.parse("image/*")
            val requestFile = RequestBody.create(mediaType, file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            
            val textMediaType = MediaType.parse("text/plain")
            val preset = RequestBody.create(textMediaType, UPLOAD_PRESET)

            val response = service.uploadImage(
                file = body,
                uploadPreset = preset
            )

            if (response.isSuccessful) {
                response.body()?.secure_url
            } else {
                println("Cloudinary upload failed: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
