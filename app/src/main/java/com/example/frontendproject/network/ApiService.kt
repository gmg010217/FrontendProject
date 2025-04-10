package com.example.frontendproject.network

import com.example.frontendproject.model.Diary
import com.example.frontendproject.model.Exercise
import com.example.frontendproject.model.LoginRequest
import com.example.frontendproject.model.LoginResponse
import com.example.frontendproject.model.MemberInfoResponse
import com.example.frontendproject.model.MemberUpdate
import com.example.frontendproject.model.Quote
import com.example.frontendproject.model.SignUpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("member/edit/{id}")
    fun getMemberInfo(@Path("id") memberId: Long): Call<MemberUpdate>

    @POST("member/edit/{id}")
    fun updateMember(@Path("id") memberId: Long, @Body request: MemberUpdate): Call<String>

    @GET("diary/{id}")
    fun getDiaryList(@Path("id") memberId: Long): Call<List<Diary>>

    @GET("diary/{id}/{diaryid}")
    fun getDiaryDetail(@Path("id") id: Long, @Path("diaryid") diaryId: Long): Call<Diary>

    @PUT("diary/{id}/{diaryid}")
    fun updateDiary(@Path("id") id: Long, @Path("diaryid") diaryId: Long, @Body request: Diary): Call<String>

    @DELETE("diary/{id}/{diaryid}")
    fun deleteDiary(@Path("id") id: Long, @Path("diaryid") diaryId: Long): Call<String>

    @POST("diary/{id}")
    fun addDiary(@Path("id") id: Long, @Body request: Diary): Call<String>

    @DELETE("member/{id}")
    fun deleteMember(@Path("id") id: Long): Call<String>

    @GET("exercise/{dayid}")
    fun getQuote(@Path("dayid") id: Long): Call<Quote>

    @GET("exercise/{id}/{date}")
    fun getExercise(@Path("id") memberId: Long, @Path("date") date: String): Call<Exercise>

    @POST("exercise/{id}")
    fun addExercise(@Path("id") memberId: Long, @Body exercise: Exercise): Call<String>

    @POST("exericse/{id}/{date}")
    fun editExercises(@Path("id") memberId: Long, @Path("date") date: String, @Body exercise: Exercise): Call<String>

}