package com.example.frontendproject.model.counselboard

data class CounselBoardResponse(
    val writerName: String,
    val writerId: Long,
    val title: String,
    val content: String,
    val comments: List<CommentResponse>
)
