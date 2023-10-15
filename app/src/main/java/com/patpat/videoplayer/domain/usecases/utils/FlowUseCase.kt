package com.patpat.videoplayer.domain.usecases.utils

import kotlinx.coroutines.flow.Flow

interface FlowUseCase<in INPUT, OUTPUT> {
    fun execute(params: INPUT): Flow<ResultState<OUTPUT>>
}