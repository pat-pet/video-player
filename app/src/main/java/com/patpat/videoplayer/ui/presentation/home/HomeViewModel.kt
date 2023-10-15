package com.patpat.videoplayer.ui.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patpat.videoplayer.domain.models.ContentVideoModel
import com.patpat.videoplayer.domain.usecases.GetContentVideosUseCase
import com.patpat.videoplayer.domain.usecases.utils.None
import com.patpat.videoplayer.domain.usecases.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getContentVideosUseCase: GetContentVideosUseCase
) : ViewModel() {

    var contentVideos by mutableStateOf<List<ContentVideoModel>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun initData() {
        viewModelScope.launch {
            getContentVideosUseCase.execute(None).collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        withContext(Dispatchers.Main) {
                            isLoading = false
                        }
                    }

                    ResultState.Loading -> {
                        withContext(Dispatchers.Main) {
                            isLoading = true
                        }
                    }

                    is ResultState.Success -> {
                        withContext(Dispatchers.Main) {
                            contentVideos = it.data
                            isLoading = false
                        }
                    }
                }
            }
        }
    }
}