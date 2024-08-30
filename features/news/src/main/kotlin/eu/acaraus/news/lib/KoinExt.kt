package eu.acaraus.news.lib

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.LocalKoinScope
import org.koin.compose.currentKoinScope
import org.koin.compose.koinInject
import org.koin.core.Koin
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.getScopeId
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.viewmodel.defaultExtras
import org.koin.viewmodel.resolveViewModel
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

// ### start @InjectedParam based injection

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

// ### end @InjectedParam based injection

// ### start scope based injection

@OptIn(KoinInternalApi::class)
inline fun <reified VM : ViewModel, reified KS : Any> Koin.scopedKoinViewModel(
    scopeId: String = UUID.randomUUID().toString(),
    viewModelCoroutineScope: CoroutineScope = get<CoroutineScope>(qualifier = named("viewModelCoroutineScope")),
    viewModelStoreOwner: ViewModelStoreOwner,
    key: String? = null,
    extras: CreationExtras = defaultExtras(viewModelStoreOwner),
    noinline parameters: ParametersDefinition? = null,
): Pair<VM, Scope> {
    val viewModelKoinScope = getOrCreateScope<KS>(scopeId = scopeId).apply {
        declare(instance = viewModelCoroutineScope, allowOverride = true)
    }
    val vm = resolveViewModel(
        VM::class,
        viewModelStoreOwner.viewModelStore,
        key,
        extras,
        null,
        viewModelKoinScope,
        parameters,
    )
    return vm to viewModelKoinScope
}

@Composable
inline fun <reified VM : ViewModel, reified KS : Any> scopedKoinViewModel(
    noinline parameters: ParametersDefinition? = null,
): VM {
    val koin: Koin =
        LocalKoinScope.current.getKoin() as? Koin ?: throw Exception("KoinComponent no found")
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
        ?: throw Exception("ViewModelStoreOwner ${VM::class.simpleName} not found")
    val (vm, viewModelScope) = koin.scopedKoinViewModel<VM, KS>(
        scopeId = viewModelStoreOwner.viewModelStore.getScopeId(),
        viewModelStoreOwner = viewModelStoreOwner,
        parameters = parameters,
    )
    KoinScopedViewModelLifecycleEnd { viewModelScope.close() }
    return vm
}

@Composable
fun KoinScopedViewModelLifecycleEnd(
    onEnd: () -> Unit,
) {
    val lifecycleOwner = LocalViewModelStoreOwner.current as? LifecycleOwner ?: return
    val activityContext = LocalContext.current as? Activity ?: return
    LaunchedEffect(Unit) {
        var observer: LifecycleEventObserver? = null
        observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                if (activityContext.isChangingConfigurations.not()) {
                    onEnd()
                }
                observer?.let { item -> lifecycleOwner.lifecycle.removeObserver(item) }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }
}

// ### end scope based injection
