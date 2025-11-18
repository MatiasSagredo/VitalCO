package com.example.vitalco.data.remote

import com.example.vitalco.data.model.Product
import retrofit2.http.GET

interface ApiService {
    @GET("/product")
    suspend fun getProducts(): List<Product>
}
