package com.patpat.videoplayer.domain.repository

import com.patpat.videoplayer.domain.models.CommentModel
import com.patpat.videoplayer.domain.models.ContentVideoModel
import com.patpat.videoplayer.domain.usecases.utils.None
import kotlinx.coroutines.flow.Flow

interface ContentVideoRepository {
    suspend fun getContentVideos(): List<ContentVideoModel>
    fun getComments(videoId: String): Flow<List<CommentModel>>
    fun sendComment(videoId: String, comment: String): Flow<None>
    fun getContentVideo(videoId: String): Flow<ContentVideoModel>
    fun addLikes(videoId: String): Flow<None>
    fun minLikes(videoId: String): Flow<None>
}