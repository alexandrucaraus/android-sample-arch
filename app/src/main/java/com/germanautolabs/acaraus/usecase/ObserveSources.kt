package com.germanautolabs.acaraus.usecase

import com.germanautolabs.acaraus.models.ArticleSource
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Factory

@Factory
class ObserveSources {
    fun stream(): Flow<Result<List<ArticleSource>, Error>> = flow {
        emit(Result.success(listOf(ArticleSource("test"))))
    }
}
