package com.patpat.videoplayer.data.models

data class ContentVideoResponse(
    val id: String?,
    val url: String?,
    val title: String?,
    val subtitle: String?,
    val likes: Int?,
    val dislikes: Int?,
    val thumbnailUrl: String?
) {
    companion object {
        const val REFERENCE_NAME = "content_videos"
    }
}