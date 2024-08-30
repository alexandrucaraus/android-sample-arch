package eu.acaraus.news.presentation.articles.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.lib.serializableNavType
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
