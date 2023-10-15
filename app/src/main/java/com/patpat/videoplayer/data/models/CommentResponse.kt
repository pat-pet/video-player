package com.patpat.videoplayer.data.models

import java.time.LocalDate

data class CommentResponse(
    val content: String?,
    val createdAt: LocalDate?
) {
    companion object {
        const val REFERENCE_NAME = "comments"
    }
}