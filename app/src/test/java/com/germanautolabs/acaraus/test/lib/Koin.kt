package com.germanautolabs.acaraus.test.lib

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.test.KoinTest

// TODO better naming , use more from the real function, or wrap the real function in a composable
inline fun <reified VM : ViewModel, reified KoinScope : Any> KoinTest.scopedKoinViewModel(
    scope: CoroutineScope? = null,
    noinline parameters: ParametersDefinition? = null,
): VM {
    val koinScope = getKoin().createScope<KoinScope>().apply {
        declare(instance = scope, allowOverride = true)
    }
    val vm = koinScope.inject<VM>(
        parameters = parameters,
    )

    return vm.value
}

inline fun <reified VM : Any, reified KoinScope : Any> KoinTest.testScopedInject(
    scope: CoroutineScope? = null,
    noinline parameters: ParametersDefinition? = null,
): VM {
    val koinScope = getKoin().createScope<KoinScope>().apply {
        declare(instance = scope, allowOverride = true)
    }
    val vm = koinScope.inject<VM>(
        parameters = parameters,
    )

    return vm.value
}
