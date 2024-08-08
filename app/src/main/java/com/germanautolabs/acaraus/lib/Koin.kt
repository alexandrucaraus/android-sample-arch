package com.germanautolabs.acaraus.lib

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.currentKoinScope
import org.koin.compose.koinInject
import org.koin.core.annotation.InjectedParam
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

@Composable
inline fun <reified T : ViewModel> scopedKoinViewModel(
    providedParams: List<Any> = emptyList(),
): T {
    val koinScope = currentKoinScope()
    val coroutineScope = koinInject<CoroutineScope>(qualifier = named("main"))
    val dependencies = getInjectedParamDependencies<T>()
        .filterNot { clazz -> providedParams.any { it::class == clazz } }
        .map { clazz -> koinScope.get<Any>(clazz) { parametersOf(coroutineScope) } }
        .plus(providedParams)
    return koinViewModel<T> { parametersOf(*dependencies.toTypedArray(), coroutineScope) }
}

inline fun <reified T : ViewModel> getInjectedParamDependencies(
    annotationClass: KClass<out Annotation> = InjectedParam::class,
): List<KClass<*>> =
    T::class.primaryConstructor?.parameters
        ?.filter { it.findAnnotation<Annotation>()?.annotationClass == annotationClass }
        ?.map { parameters -> parameters.type.classifier as? KClass<*> ?: Any::class }
        ?: emptyList()
