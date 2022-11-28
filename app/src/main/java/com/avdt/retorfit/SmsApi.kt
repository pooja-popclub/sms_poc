package com.avdt.retorfit

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface SmsApi {
    @POST("/api/v1/category-transaction/add")
    fun sendSmsList(@Body map: HashMap<String, Any>): Call<ApiResponse>

    @POST("/api/v1/user/authenticate")
    fun loginApi(@Body map: HashMap<String, Any>): Call<ApiResponse>

    @Headers("os_type: android")
    @GET(" /api/v1/category-transaction/coins")
    fun getDataFromTheServer(): Call<ApiResponse1>
}