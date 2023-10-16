package com.patpat.videoplayer.data.network

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import com.patpat.videoplayer.data.models.CommentResponse
import com.patpat.videoplayer.data.models.ContentVideoResponse
import com.patpat.videoplayer.domain.usecases.utils.None
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor() : NetworkDataSource {

    private val firestore = Firebase.firestore

    override fun addVideoLike(videoId: String): Flow<None> {
        return flow {
            val video = firestore.collection(ContentVideoResponse.REFERENCE_NAME)
                .document(videoId)
                .get()
                .await()

            firestore.collection(ContentVideoResponse.REFERENCE_NAME)
                .document(videoId)
                .update(
                    "likes",
                    video.get("likes").toString().toDouble().toInt().plus(1).toDouble()
                )
                .await()

            emit(None)
        }
    }

    override fun minVideoLike(videoId: String): Flow<None> {
        return flow {
            val video = firestore.collection(ContentVideoResponse.REFERENCE_NAME)
                .document(videoId)
                .get()
                .await()

            firestore.collection(ContentVideoResponse.REFERENCE_NAME)
                .document(videoId)
                .update(
                    "dislikes",
                    video.get("dislikes").toString().toDouble().plus(1).toDouble()
                )
                .await()

            emit(None)
        }
    }

    override fun addComment(videoId: String, comment: String): Flow<None> {
        return flow {
            val video = firestore.collection(ContentVideoResponse.REFERENCE_NAME)
                .document(videoId)

            val comment = hashMapOf(
                "content" to comment,
                "content_videos" to video,
                "created_at" to Date()
            )

            firestore.collection(CommentResponse.REFERENCE_NAME)
                .document()
                .set(comment)
                .await()

            emit(None)
        }
    }

    override fun observeVideoContent(videoId: String): Flow<ContentVideoResponse> {
        return firestore.collection(ContentVideoResponse.REFERENCE_NAME)
            .document(videoId)
            .snapshots()
            .map {
                ContentVideoResponse(
                    url = it.getString("url"),
                    title = it.getString("title"),
                    subtitle = it.getString("subtitle"),
                    likes = it.get("likes").toString().toDouble().toInt(),
                    dislikes = it.get("dislikes").toString().toDouble().toInt(),
                    thumbnailUrl = it.getString("thumbnail_url"),
                    id = it.id
                )
            }
    }

    override fun getContentVideos(): Flow<List<ContentVideoResponse>> = flow {
        val documents = firestore.collection(ContentVideoResponse.REFERENCE_NAME)
            .get()
            .await()
            .map {
                ContentVideoResponse(
                    url = it?.getString("url"),
                    title = it?.getString("title"),
                    subtitle = it?.getString("subtitle"),
                    likes = it?.get("likes").toString().toDouble().toInt(),
                    dislikes = it?.get("dislikes").toString().toDouble().toInt(),
                    thumbnailUrl = it?.getString("thumbnail_url"),
                    id = it?.id
                )
            }

        emit(documents)
    }

    override fun observeComments(videoId: String): Flow<List<CommentResponse>> {
        val videoReference = firestore
            .collection(ContentVideoResponse.REFERENCE_NAME)
            .document(videoId)

        return firestore.collection(CommentResponse.REFERENCE_NAME)
            .whereEqualTo("content_videos", videoReference)
            .orderBy("created_at", Query.Direction.DESCENDING)
            .snapshots()
            .map {
                it.documents.map { snapshot ->
                    val createdAt = snapshot.getDate("created_at") ?: Date()
                    Timber.d("Created At $createdAt")
                    CommentResponse(
                        content = snapshot.getString("content"),
                        createdAt = LocalDate.ofInstant(
                            createdAt.toInstant(),
                            ZoneId.systemDefault()
                        )
                    )
                }
            }
    }
}