package com.loginapp.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("login")
    suspend fun login(@Body body: Map<String, String>): Response<ResponseBody>

    @POST("users")
    suspend fun signUp(@Body body: Map<String, String>): Response<ResponseBody>

    companion object{
        const val BASE_URL = "https://userservice-qeo1.onrender.com/"
    }
}