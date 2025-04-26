package com.example.frontendproject.model.freeboard

data class CommentResponse (
    val id: Long,
    val writerName: String,
    val content: String,
    val writerId: Long
)