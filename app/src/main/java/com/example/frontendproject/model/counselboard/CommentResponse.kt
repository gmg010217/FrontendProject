package com.example.frontendproject.model.counselboard

data class CommentResponse (
    val id: Long,
    val writerName: String,
    val content: String,
    val writerId: Long
)