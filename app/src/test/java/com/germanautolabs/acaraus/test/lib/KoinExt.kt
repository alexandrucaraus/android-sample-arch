package com.germanautolabs.acaraus.test.lib

import androidx.lifecycle.ViewModel
import com.germanautolabs.acaraus.lib.scopedKoinViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.core.scope.Scope
import org.koin.test.KoinTest

inline fun <reified V : ViewModel, reified S : Any> KoinTest.injectScopedViewModel(
    coroutineScope: CoroutineScope,
): Pair<V, Scope> = getKoin().scopedKoinViewModel<V, S>(
    viewModelStoreOwner = testViewModelStoreOwner(),
    viewModelCoroutineScope = coroutineScope,
)

inline fun <reified D : Any, reified S : Any> KoinTest.injectScoped(
    coroutineScope: CoroutineScope,
    scopeId: String = "koinTestScope",
): Pair<D, Scope> {
    val customKoinScope = this.getKoin().createScope<S>(scopeId = scopeId).apply {
        declare(instance = coroutineScope, allowOverride = true)
    }
    return customKoinScope.get<D>() to customKoinScope
}
