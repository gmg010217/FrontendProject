package com.example.frontendproject.network

import com.example.frontendproject.model.AiChatRequest
import com.example.frontendproject.model.AiChatResponse
import com.example.frontendproject.model.Diary
import com.example.frontendproject.model.Exercise
import com.example.frontendproject.model.ExerciseCountResponse
import com.example.frontendproject.model.LoginRequest
import com.example.frontendproject.model.LoginResponse
import com.example.frontendproject.model.MemberInfoResponse
import com.example.frontendproject.model.MemberUpdate
import com.example.frontendproject.model.Quiz
import com.example.frontendproject.model.QuizDto
import com.example.frontendproject.model.Quote
import com.example.frontendproject.model.SignUpRequest
import com.example.frontendproject.model.counselboard.CounselBoardAddRequest
import com.example.frontendproject.model.counselboard.CounselBoardCommentAddRequest
import com.example.frontendproject.model.counselboard.CounselBoardResponse
import com.example.frontendproject.model.counselboard.CounselboardsResponse
import com.example.frontendproject.model.freeboard.FreeBoardAddRequest
import com.example.frontendproject.model.freeboard.FreeBoardCommentAddRequest
import com.example.frontendproject.model.freeboard.FreeBoardResponse
import com.example.frontendproject.model.freeboard.FreeboardsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("exercise/main/{id}/{dayid}")
    fun getQuoteAndRecommand(@Path("id") memberId: Long, @Path("dayid") id: Long): Call<Quote>

    @GET("exercise/{id}/{date}")
    fun getExercise(@Path("id") memberId: Long, @Path("date") date: String): Call<Exercise>

    @GET("exercise/first/{id}/{date}")
    fun getFirstExercise(@Path("id") memberId: Long, @Path("date") date: String?): Call<Exercise>

    @GET("exercise/first/{id}")
    fun isFirstExercise(@Path("id") memberId: Long): Call<ExerciseCountResponse>

    @POST("exercise/{id}")
    fun addExercise(@Path("id") memberId: Long, @Body exercise: Exercise): Call<String>

    @POST("exercise/{id}/{date}")
    fun editExercises(@Path("id") memberId: Long, @Path("date") date: String?, @Body exercise: Exercise): Call<String>

    @GET("freeboard/")
    fun getFreeBoardList() : Call<List<FreeboardsResponse>>

    @POST("freeboard/{id}")
    fun addFreeBoard(@Path("id") memberId: Long, @Body freeBoardAddRequest: FreeBoardAddRequest): Call<String>

    @GET("freeboard/search")
    fun searchFreeBoardList(@Query("title") title: String): Call<List<FreeboardsResponse>>

    @GET("freeboard/{id}/{boardid}")
    fun getFreeboard(@Path("id") memberId: Long, @Path("boardid") boardId: Long): Call<FreeBoardResponse>

    @GET("freeboard/edit/{id}/{boardid}")
    fun getEditFreeBoard(@Path("id") memberId: Long, @Path("boardid") boardId: Long): Call<FreeBoardAddRequest>

    @POST("freeboard/edit/{id}/{boardid}")
    fun editFreeBoard(@Path("id") memberId: Long, @Path("boardid") boardId: Long, @Body freeBoardAddRequest : FreeBoardAddRequest): Call<String>

    @DELETE("freeboard/{id}/{boardid}")
    fun deleteFreeBoard(@Path("id") memberId: Long, @Path("boardid") boardId: Long): Call<String>

    @DELETE("freeboard/comment/{id}/{commentid}")
    fun deleteFreeBoardCommnet(@Path("id") memberId: Long, @Path("commentid") commentId: Long): Call<String>

    @POST("freeboard/comment/{id}/{boardid}")
    fun addFreeBoardComment(@Path("id") memberId: Long, @Path("boardid") boardId: Long, @Body freeBoardCommentAddRequest: FreeBoardCommentAddRequest): Call<String>

    @GET("counselboard/")
    fun getCounselBoardList() : Call<List<CounselboardsResponse>>

    @POST("counselboard/{id}")
    fun addCounselBoard(@Path("id") memberId: Long, @Body counselBoardAddRequest: CounselBoardAddRequest): Call<String>

    @GET("counselboard/search")
    fun searchCounselBoardList(@Query("title") title: String): Call<List<CounselboardsResponse>>

    @GET("counselboard/{id}/{boardid}")
    fun getCounselboard(@Path("id") memberId: Long, @Path("boardid") boardId: Long): Call<CounselBoardResponse>

    @GET("counselboard/edit/{id}/{boardid}")
    fun getEditCounselBoard(@Path("id") memberId: Long, @Path("boardid") boardId: Long): Call<CounselBoardAddRequest>

    @POST("counselboard/edit/{id}/{boardid}")
    fun editCounselBoard(@Path("id") memberId: Long, @Path("boardid") boardId: Long, @Body counselBoardAddRequest : CounselBoardAddRequest): Call<String>

    @DELETE("counselboard/{id}/{boardid}")
    fun deleteCounselBoard(@Path("id") memberId: Long, @Path("boardid") boardId: Long): Call<String>

    @DELETE("counselboard/comment/{id}/{commentid}")
    fun deleteCounselBoardCommnet(@Path("id") memberId: Long, @Path("commentid") commentId: Long): Call<String>

    @POST("counselboard/comment/{id}/{boardid}")
    fun addCounselBoardComment(@Path("id") memberId: Long, @Path("boardid") boardId: Long, @Body counselBoardCommentAddRequest: CounselBoardCommentAddRequest): Call<String>

    @GET("aichat/{id}")
    fun getAiChat(@Path("id") memberId: Long): Call<List<AiChatResponse>>

    @POST("aichat/{id}")
    fun addAiChat(@Path("id") memberId: Long, @Body message : AiChatRequest): Call<String>

    @GET("quiz/{id}")
    fun getQuiz(@Path("id") memberId: Long): Call<List<QuizDto>>

    @POST("quiz/save/{id}")
    fun saveQuiz(@Path("id") memberId: Long, @Body quiz: Quiz): Call<String>
}