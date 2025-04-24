package com.example.frontendproject.model

data class CommentResponse (
    val id: Long,
    val writerName: String,
    val content: String,
    val writerId: Long
)