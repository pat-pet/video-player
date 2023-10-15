package com.patpat.videoplayer.data.repository

import com.patpat.videoplayer.data.network.NetworkDataSource
import com.patpat.videoplayer.domain.models.CommentModel
import com.patpat.videoplayer.domain.models.ContentVideoModel
import com.patpat.videoplayer.domain.repository.ContentVideoRepository
import com.patpat.videoplayer.domain.usecases.utils.None
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class ContentVideoRepositoryImpl @Inject constructor(
    private val dataSource: NetworkDataSource
) : ContentVideoRepository {

    override suspend fun getContentVideos(): List<ContentVideoModel> {
        return dataSource.getContentVideos()
            .map { ContentVideoModel.from(it) }
            .single()
    }

    override fun getComments(videoId: String): Flow<List<CommentModel>> {
        return dataSource.observeComments(videoId)
            .map {
                CommentModel.from(it)
            }
    }

    override fun sendComment(videoId: String, comment: String): Flow<None> {
        return dataSource.addComment(videoId, comment)
    }

    override fun addLikes(videoId: String): Flow<None> {
        return dataSource.addVideoLike(videoId)
    }

    override fun minLikes(videoId: String): Flow<None> {
        return dataSource.minVideoLike(videoId)
    }

    override fun getContentVideo(videoId: String): Flow<ContentVideoModel> {
        return dataSource.observeVideoContent(videoId).map { ContentVideoModel.from(it) }
    }
}