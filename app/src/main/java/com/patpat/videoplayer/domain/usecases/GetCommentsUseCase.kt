package com.patpat.videoplayer.domain.usecases

import com.patpat.videoplayer.domain.models.CommentModel
import com.patpat.videoplayer.domain.repository.ContentVideoRepository
import com.patpat.videoplayer.domain.usecases.utils.FlowUseCase
import com.patpat.videoplayer.domain.usecases.utils.ResultState
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class GetCommentsUseCase @Inject constructor(
    private val repository: ContentVideoRepository
) : FlowUseCase<GetCommentsUseCase.Params, List<CommentModel>> {

    override fun execute(params: Params): Flow<ResultState<List<CommentModel>>> {
        return repository.getComments(params.videoId)
            .map { ResultState.Success(it) }
            .catch { ResultState.Error(it) }
            .flowOn(Dispatchers.IO)
    }

    data class Params(val videoId: String)
}