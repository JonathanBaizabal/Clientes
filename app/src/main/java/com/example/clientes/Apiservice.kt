package com.example.clientes

import com.example.clientes.model.Cliente
import com.example.clientes.model.ClienteItem
import com.example.clientes.model.Res_Data
import okhttp3.RequestBody
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @GET("/api/clientes")
    fun getData(): Call<List<ClienteItem>>

    @GET("/api/clientes/{id}")
    fun getDataid(@Path("id")id: Int): Call<ClienteItem>

    @POST("/api/clientes/add")
    suspend fun clienteAdd(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("/api/clientes/delete/{id}")
    fun getDataDelete(@Path("id")id: Int):Call<Res_Data>

}