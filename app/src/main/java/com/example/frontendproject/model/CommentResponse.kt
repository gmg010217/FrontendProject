package com.example.frontendproject.model

data class CommentResponse (
    val writerName: String,
    val content: String,
    val writerId: Long
)