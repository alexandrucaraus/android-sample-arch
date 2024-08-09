package com.germanautolabs.acaraus.lib

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.currentKoinScope
import org.koin.compose.koinInject
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.KoinComponent
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
    val coroutineScope = koinScope.get<CoroutineScope>(qualifier = named("main"))
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
