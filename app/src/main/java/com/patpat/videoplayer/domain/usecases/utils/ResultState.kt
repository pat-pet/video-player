package com.patpat.videoplayer.domain.usecases.utils

sealed class ResultState<out T> {
    object Loading: ResultState<Nothing>()
    data class Success<T>(val data: T): ResultState<T>()
    data class Error(val exception: Throwable): ResultState<Nothing>()
}