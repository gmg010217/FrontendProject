package com.example.frontendproject.model

data class FreeBoardResponse(
    val writerName: String,
    val writerId: Long,
    val title: String,
    val content: String,
    val comments: List<CommentResponse>
)
