package com.example.frontendproject.network

import com.example.frontendproject.model.LoginRequest
import com.example.frontendproject.model.LoginResponse
import com.example.frontendproject.model.SignUpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/healthmind/member/signup")
    fun signUp(@Body request: SignUpRequest): Call<String>

    @POST("/healthmind/member/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}