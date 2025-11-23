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

    @GET("clientes") suspend fun getClientes(): Response<List<Clientes>>
    @GET("clientes/{id}") suspend fun getClienteById(@Path("id") id: Int): Response<Clientes>
    @POST("clientes") suspend fun createClientes(@Body cliente: Clientes): Response<Clientes>
    @PUT("clientes/{id}") suspend fun updateClientes(@Path("id") id: Int, @Body cliente: Clientes): Response<Clientes>
    @DELETE("clientes/{id}") suspend fun deleteClientes(@Path("id") id: Int): Response<Unit>

    @GET("proveedores") suspend fun getProveedores(): Response<List<Proveedores>>
    @GET("proveedores/{id}") suspend fun getProveedorById(@Path("id") id: Int): Response<Proveedores>
    @POST("proveedores") suspend fun createProveedores(@Body proveedor: Proveedores): Response<Proveedores>
    @PUT("proveedores/{id}") suspend fun updateProveedores(@Path("id") id: Int, @Body proveedor: Proveedores): Response<Proveedores>
    @DELETE("proveedores/{id}") suspend fun deleteProveedores(@Path("id") id: Int): Response<Unit>

    @GET("compras") suspend fun getCompras(): Response<List<Compras>>
    @GET("compras/{id}") suspend fun getCompraById(@Path("id") id: Int): Response<Compras>
    @POST("compras") suspend fun createCompras(@Body compras: Compras): Response<Compras>
    @PUT("compras/{id}") suspend fun updateCompras(@Path("id") id: Int, @Body compras: Compras): Response<Compras>
    @DELETE("compras/{id}") suspend fun deleteCompras(@Path("id") id: Int): Response<Unit>

    @GET("ventas") suspend fun getVentas(): Response<List<Ventas>>
    @GET("ventas/{id}") suspend fun getVentaById(@Path("id") id: Int): Response<Ventas>
    @POST("ventas") suspend fun createVentas(@Body venta: Ventas): Response<Ventas>
    @PUT("ventas/{id}") suspend fun updateVentas(@Path("id") id: Int, @Body venta: Ventas): Response<Ventas>
    @DELETE("ventas/{id}") suspend fun deleteVentas(@Path("id") id: Int): Response<Unit>

    @GET("detalle_compras") suspend fun getDetalleCompras(): Response<List<DetalleCompras>>
    @GET("detalle_compras/{id}") suspend fun getDetalleComprasById(@Path("id") id: Int): Response<DetalleCompras>
    @POST("detalle_compras") suspend fun createDetalleCompras(@Body detalleCompras: DetalleCompras): Response<DetalleCompras>
    @PUT("detalle_compras/{id}") suspend fun updateDetalleCompras(@Path("id") id: Int, @Body detalleCompras: DetalleCompras): Response<DetalleCompras>
    @DELETE("detalle_compras/{id}") suspend fun deleteDetalleCompras(@Path("id") id: Int): Response<Unit>

    @GET("detalle_ventas") suspend fun getDetalleVentas(): Response<List<DetalleVentas>>
    @GET("detalle_ventas/{id}") suspend fun getDetalleVentasById(@Path("id") id: Int): Response<DetalleVentas>
    @POST("detalle_ventas") suspend fun createDetalleVentas(@Body detalleVentas: DetalleVentas): Response<DetalleVentas>
    @PUT("detalle_ventas/{id}") suspend fun updateDetalleVentas(@Path("id") id: Int, @Body detalleVentas: DetalleVentas): Response<DetalleVentas>
    @DELETE("detalle_ventas/{id}") suspend fun deleteDetalleVentas(@Path("id") id: Int): Response<Unit>

    @GET("movimientos_stock") suspend fun getMovimientosStock(): Response<List<MovimientosStock>>
    @GET("movimientos_stock/{id}") suspend fun getMovimientosStockById(@Path("id") id: Int): Response<MovimientosStock>
    @POST("movimientos_stock") suspend fun createMovimientosStock(@Body movimiento: MovimientosStock): Response<MovimientosStock>
    @PUT("movimientos_stock/{id}") suspend fun updateMovimientosStock(@Path("id") id: Int, @Body movimiento: MovimientosStock): Response<MovimientosStock>
    @DELETE("movimientos_stock/{id}") suspend fun deleteMovimientosStock(@Path("id") id: Int): Response<Unit>

    @GET("usuarios") suspend fun getUsuarios(): Response<List<Usuarios>>
    @GET("usuarios/{id}") suspend fun getUsuarioById(@Path("id") id: Int): Response<Usuarios>
    @PUT("usuarios/{id}") suspend fun updateUsuarios(@Path("id") id: Int, @Body usuarios: Usuarios): Response<Usuarios>
    @DELETE("usuarios/{id}") suspend fun deleteUsuarios(@Path("id") id: Int): Response<Unit>

    @POST("auth/login") suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
    @POST("auth/register") suspend fun register(@Body registerRequest: RegisterRequest): Response<Usuarios>
}

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String, val user: Usuarios)
data class RegisterRequest(val username: String, val email: String, val password: String)

