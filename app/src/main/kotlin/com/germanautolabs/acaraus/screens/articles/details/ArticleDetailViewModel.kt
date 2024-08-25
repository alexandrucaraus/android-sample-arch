package com.germanautolabs.acaraus.screens.articles.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.germanautolabs.acaraus.lib.serializableNavType
import com.germanautolabs.acaraus.models.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Scope
import kotlin.reflect.typeOf

@Scope
class ArticleDetailKoinScope

@KoinViewModel
@Scope(ArticleDetailKoinScope::class)
class ArticleDetailViewModel(
    handle: SavedStateHandle,
    coroutineScope: CoroutineScope,
) : ViewModel(coroutineScope) {

    val state = MutableStateFlow(
        handle.toRoute<ArticleDetailNode>(
            typeMap = mapOf(typeOf<Article>() to serializableNavType<Article>()),
        ).article,
    )
}