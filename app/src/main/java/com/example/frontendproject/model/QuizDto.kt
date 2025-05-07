package com.example.frontendproject.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizDto(
    val question: String,
    val options: List<String>,
    val answer: String
) : Parcelable
