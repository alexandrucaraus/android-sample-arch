package eu.acaraus.news.lib

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import kotlinx.coroutines.Dispatchers as KotlinProvidedDispatchers

interface DispatcherProvider {
    val io: CoroutineDispatcher
    val cpu: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
    val ui: CoroutineDispatcher
}

@Single(binds = [DispatcherProvider::class])
class DispatcherProviderApp : DispatcherProvider {
    override val io: CoroutineDispatcher get() = KotlinProvidedDispatchers.IO
    override val cpu: CoroutineDispatcher get() = KotlinProvidedDispatchers.Default
    override val unconfined: CoroutineDispatcher get() = KotlinProvidedDispatchers.Unconfined
    override val ui: CoroutineDispatcher get() = KotlinProvidedDispatchers.Main.immediate
}

@Factory
@Named("viewModelCoroutineScope")
fun uiMainCoroutineScope(dispatcherProvider: DispatcherProvider): CoroutineScope =
    CoroutineScope(SupervisorJob() + dispatcherProvider.ui)
