package com.germanautolabs.acaraus.lib

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.LocalKoinScope
import org.koin.compose.currentKoinScope
import org.koin.compose.koinInject
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

@Composable
inline fun <reified T : ViewModel> scopedKoinViewModel(
    vararg params: Any,
): T {
    val koinScope = currentKoinScope()
    val coroutineScope = koinInject<CoroutineScope>(qualifier = named("main"))
    val dependencies = getInjectedParamDependencies<T>()
        .filterNot { clazz -> params.any { provided -> provided::class == clazz } }
        .map { clazz -> koinScope.get<Any>(clazz) { parametersOf(coroutineScope) } }
        .plus(params)
    return koinViewModel<T> { parametersOf(*dependencies.toTypedArray(), coroutineScope) }
}

inline fun <reified T : Any> KoinComponent.scopedKoinInject(
    vararg params: Any,
): T {
    val koinScope = this.getKoin()
    val coroutineScope =
        if (params.any { it is CoroutineScope }) {
            params.first { it is CoroutineScope }
        } else {
            koinScope.get<CoroutineScope>(qualifier = named("main"))
        }
    val dependencies = getInjectedParamDependencies<T>()
        .filterNot { clazz -> params.any { provided -> provided::class == clazz } }
        .map { clazz -> koinScope.get<Any>(clazz) { parametersOf(coroutineScope) } }
        .plus(params)
    return koinScope.get<T> { parametersOf(*dependencies.toTypedArray(), coroutineScope) }
}

inline fun <reified T : Any> getInjectedParamDependencies(
    annotationClass: KClass<out Annotation> = InjectedParam::class,
): List<KClass<*>> =
    T::class.primaryConstructor?.parameters
        ?.filter { it.findAnnotation<Annotation>()?.annotationClass == annotationClass }
        ?.map { parameters -> parameters.type.classifier as? KClass<*> ?: Any::class }
        ?: emptyList()

@Composable
inline fun <reified VM : ViewModel, reified KoinScope : Any> scopedKoinViewModel(
    noinline parameters: ParametersDefinition? = null,
): VM {
    val coroutineScope = koinInject<CoroutineScope>(named("main"))
    val koinScope = LocalKoinScope.current.getKoin().createScope<KoinScope>().apply {
        declare(instance = coroutineScope, allowOverride = true)
    }
    val vm = koinViewModel<VM>(
        scope = koinScope,
        parameters = parameters,
    )
    DestroyLifecycle { koinScope.close() }
    return vm
}

@Composable
fun DestroyLifecycle(
    onDestroy: () -> Unit,
) {
    val lifecycleOwner = LocalViewModelStoreOwner.current as? LifecycleOwner ?: return
    val activityContext = LocalContext.current as? Activity ?: return

    LaunchedEffect(Unit) {
        var observer: LifecycleEventObserver? = null
        observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                if (activityContext.isChangingConfigurations.not()) {
                    onDestroy()
                }
                observer?.let { item -> lifecycleOwner.lifecycle.removeObserver(item) }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }
}
