package com.example.vitalco.data.remote

import com.example.vitalco.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("productos") suspend fun getProductos(): Response<List<Productos>>
    @GET("productos/{id}") suspend fun getProductoById(@Path("id") id: Int): Response<Productos>
    @POST("productos") suspend fun createProductos(@Body productos: Productos): Response<Productos>
    @PUT("productos/{id}")
    suspend fun updateProductos(@Path("id") id: Int, @Body productos: Productos): Response<Productos>
    @DELETE("productos/{id}") suspend fun deleteProductos(@Path("id") id: Int): Response<Unit>

    @GET("movimientos_stock") suspend fun getMovimientosStock(): Response<List<MovimientosStock>>
    @GET("movimientos_stock/{id}") suspend fun getMovimientosStockById(@Path("id") id: Int): Response<MovimientosStock>
    @POST("movimientos_stock") suspend fun createMovimientosStock(@Body movimiento: MovimientosStock): Response<MovimientosStock>
    @PUT("movimientos_stock/{id}") suspend fun updateMovimientosStock(@Path("id") id: Int, @Body movimiento: MovimientosStock): Response<MovimientosStock>
    @DELETE("movimientos_stock/{id}") suspend fun deleteMovimientosStock(@Path("id") id: Int): Response<Unit>

    @GET("usuarios") suspend fun getUsuarios(): Response<List<Usuarios>>
    @GET("usuarios/{id}") suspend fun getUsuarioById(@Path("id") id: Int): Response<Usuarios>
    @GET("usuarios/id/{id}") suspend fun getUsuarioByIdDirect(@Path("id") id: Int): Response<Usuarios>
    @GET("usuarios/username/{name}") suspend fun getUsuarioByUsername(@Path("name") name: String): Response<Usuarios>
    @POST("usuarios") suspend fun createUsuarios(@Body usuarios: Usuarios): Response<Usuarios>
    @PUT("usuarios/{id}") suspend fun updateUsuarios(@Path("id") id: Int, @Body usuarios: UserUpdateRequest): Response<Usuarios>
    @DELETE("usuarios/{id}") suspend fun deleteUsuarios(@Path("id") id: Int): Response<Unit>
}
