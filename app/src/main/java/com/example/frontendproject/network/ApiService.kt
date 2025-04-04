package com.example.frontendproject.network

import com.example.frontendproject.model.LoginRequest
import com.example.frontendproject.model.LoginResponse
import com.example.frontendproject.model.MemberInfoResponse
import com.example.frontendproject.model.SignUpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("member/signup")
    fun signUp(@Body request: SignUpRequest): Call<String>

    @POST("member/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("member/info/{id}")
    fun memberInfo(@Path("id") id: Long): Call <MemberInfoResponse>

    @GET("member/logout")
    fun logout(): Call<String>
}