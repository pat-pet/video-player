package com.patpat.videoplayer.domain.usecases

import com.patpat.videoplayer.domain.models.ContentVideoModel
import com.patpat.videoplayer.domain.repository.ContentVideoRepository
import com.patpat.videoplayer.domain.usecases.utils.FlowUseCase
import com.patpat.videoplayer.domain.usecases.utils.None
import com.patpat.videoplayer.domain.usecases.utils.ResultState
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class GetContentVideosUseCase @Inject constructor(
    private val repository: ContentVideoRepository
) : FlowUseCase<None, List<ContentVideoModel>> {

    override fun execute(params: None): Flow<ResultState<List<ContentVideoModel>>> {
        return flow {
            emit(ResultState.Loading)
            try {
                val result = repository.getContentVideos()
                emit(ResultState.Success(result))
            } catch (e: Exception) {
                emit(ResultState.Error(e))
            }
        }
    }
}