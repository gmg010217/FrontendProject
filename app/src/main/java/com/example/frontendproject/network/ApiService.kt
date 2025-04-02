package com.example.frontendproject.network

import com.example.frontendproject.model.SignUpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/healthmind/member/signup")
    fun signUp(@Body request: SignUpRequest): Call<String>
}