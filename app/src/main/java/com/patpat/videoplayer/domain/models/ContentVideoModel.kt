package com.patpat.videoplayer.domain.models

import android.os.Parcelable
import com.patpat.videoplayer.data.models.ContentVideoResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentVideoModel(
    val id: String,
    val url: String,
    val title: String,
    val subtitle: String,
    val likes: Int,
    val dislikes: Int,
    val thumbnailUrl: String
): Parcelable {
    companion object {
        fun from(response: ContentVideoResponse?): ContentVideoModel {
            return ContentVideoModel(
                url = response?.url.orEmpty(),
                title = response?.title.orEmpty(),
                subtitle = response?.subtitle.orEmpty(),
                likes = response?.likes ?: 0,
                dislikes = response?.dislikes ?: 0,
                thumbnailUrl = response?.thumbnailUrl.orEmpty(),
                id = response?.id.orEmpty()
            )
        }

        fun from(responses: List<ContentVideoResponse>): List<ContentVideoModel> {
            return responses.map(::from)
        }
    }
}