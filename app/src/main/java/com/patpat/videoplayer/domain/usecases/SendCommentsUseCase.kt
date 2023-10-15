package com.patpat.videoplayer.domain.usecases

import com.patpat.videoplayer.domain.repository.ContentVideoRepository
import com.patpat.videoplayer.domain.usecases.utils.FlowUseCase
import com.patpat.videoplayer.domain.usecases.utils.None
import com.patpat.videoplayer.domain.usecases.utils.ResultState
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class SendCommentsUseCase @Inject constructor(
    private val repository: ContentVideoRepository
) : FlowUseCase<SendCommentsUseCase.Params, None> {

    override fun execute(params: Params): Flow<ResultState<None>> {
        return repository.sendComment(params.videoId, params.content)
            .map { ResultState.Success(it) }
            .catch { ResultState.Error(it) }
            .flowOn(Dispatchers.IO)
    }

    data class Params(val videoId: String, val content: String)
}