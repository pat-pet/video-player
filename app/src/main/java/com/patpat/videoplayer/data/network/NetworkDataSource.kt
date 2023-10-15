package com.patpat.videoplayer.data.network

import com.patpat.videoplayer.data.models.CommentResponse
import com.patpat.videoplayer.data.models.ContentVideoResponse
import com.patpat.videoplayer.domain.usecases.utils.None
import kotlinx.coroutines.flow.Flow

interface NetworkDataSource {
    fun getContentVideos(): Flow<List<ContentVideoResponse>>
    fun observeComments(videoId: String): Flow<List<CommentResponse>>
    fun addComment(videoId: String, comment: String): Flow<None>
    fun observeVideoContent(videoId: String): Flow<ContentVideoResponse>
    fun addVideoLike(videoId: String): Flow<None>
    fun minVideoLike(videoId: String): Flow<None>
}
