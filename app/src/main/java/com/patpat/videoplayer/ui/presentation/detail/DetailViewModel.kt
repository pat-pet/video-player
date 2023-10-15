package com.patpat.videoplayer.ui.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patpat.videoplayer.domain.models.CommentModel
import com.patpat.videoplayer.domain.usecases.GetCommentsUseCase
import com.patpat.videoplayer.domain.usecases.SendCommentsUseCase
import com.patpat.videoplayer.domain.usecases.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val sendCommentsUseCase: SendCommentsUseCase
) : ViewModel() {

    var comments by mutableStateOf<ResultState<List<CommentModel>>>(ResultState.Loading)
        private set

    var commentText by mutableStateOf("")
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

    fun sendComment(videoId: String) {
        viewModelScope.launch {
            sendCommentsUseCase.execute(SendCommentsUseCase.Params(videoId, commentText))
                .collectLatest {
                    commentText = ""
                }
        }
    }
}