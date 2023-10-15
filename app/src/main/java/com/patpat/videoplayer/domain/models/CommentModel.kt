package com.patpat.videoplayer.domain.models

import com.patpat.videoplayer.data.models.CommentResponse
import java.time.LocalDate

data class CommentModel(
    val content: String,
    val createdAt: LocalDate
) {
    companion object {
        private fun from(response: CommentResponse): CommentModel {
            return CommentModel(
                content = response.content.orEmpty(),
                createdAt = response.createdAt ?: LocalDate.now()
            )
        }

        fun from(responses: List<CommentResponse>): List<CommentModel> {
            return responses.map { from(it) }
        }
    }
}