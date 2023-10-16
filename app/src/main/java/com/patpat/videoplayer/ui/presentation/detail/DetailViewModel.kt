package com.patpat.videoplayer.ui.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patpat.videoplayer.domain.models.CommentModel
import com.patpat.videoplayer.domain.usecases.AddLikesUseCase
import com.patpat.videoplayer.domain.usecases.GetCommentsUseCase
import com.patpat.videoplayer.domain.usecases.GetContentVideoUseCase
import com.patpat.videoplayer.domain.usecases.MinLikesUseCase
import com.patpat.videoplayer.domain.usecases.SendCommentsUseCase
import com.patpat.videoplayer.domain.usecases.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val sendCommentsUseCase: SendCommentsUseCase,
    private val addLikesUseCase: AddLikesUseCase,
    private val minLikesUseCase: MinLikesUseCase,
    private val getContentVideoUseCase: GetContentVideoUseCase
) : ViewModel() {

    var comments by mutableStateOf<ResultState<List<CommentModel>>>(ResultState.Loading)
        private set

    var commentText by mutableStateOf("")
        private set

    var likes by mutableStateOf("0")
        private set

    var dislikes by mutableStateOf("0")
        private set

    fun updateComment(comment: String) {
        commentText = comment
    }

    fun initData(videoId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getCommentsUseCase.execute(GetCommentsUseCase.Params(videoId)).collectLatest {
                withContext(Dispatchers.Main) {
                    comments = it
                }
            }
        }
    }

    fun observeVideo(videoId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getContentVideoUseCase.execute(videoId).collectLatest {
                if (it is ResultState.Success) {
                    Timber.d("Likes: ${it.data.likes}")
                    likes = it.data.likes.toString()
                    dislikes = it.data.dislikes.toString()
                }
            }
        }
    }

    fun sendComment(videoId: String) {
        viewModelScope.launch {
            sendCommentsUseCase.execute(SendCommentsUseCase.Params(videoId, commentText))
                .collectLatest {
                    commentText = ""
                }
        }
    }

    fun addLikes(videoId: String) {
        viewModelScope.launch {
            addLikesUseCase.execute(AddLikesUseCase.Params(videoId)).collectLatest {
                if (it is ResultState.Success) {
                    Timber.d("Success")
                } else if (it is ResultState.Error) {
                    Timber.d(it.exception.localizedMessage.orEmpty())
                }
            }
        }
    }

    fun minLikes(videoId: String) {
        viewModelScope.launch {
            minLikesUseCase.execute(MinLikesUseCase.Params(videoId)).collectLatest {
                if (it is ResultState.Success) {
                    Timber.d("Success")
                } else if (it is ResultState.Error) {
                    Timber.d(it.exception.localizedMessage.orEmpty())
                }
            }
        }
    }
}