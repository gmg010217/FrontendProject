package com.example.frontendproject.model

data class SignUpRequest(
    val emailId: String,
    val password: String,
    val nickName: String,
    val age: Int,
    val gender: String,
    val aboutMe: String
)