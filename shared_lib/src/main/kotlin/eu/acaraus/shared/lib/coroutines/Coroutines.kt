package eu.acaraus.shared.lib.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers as KotlinProvidedDispatchers

interface DispatcherProvider {
    val io: CoroutineDispatcher
    val cpu: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
    val ui: CoroutineDispatcher
    fun vmCoroutineScope() = CoroutineScope(SupervisorJob() + ui)
    companion object {
        const val VM_COROUTINE_SCOPE = "VM_COROUTINE_SCOPE"
    }
}

class DispatcherProviderApp : DispatcherProvider {
    override val io: CoroutineDispatcher get() = KotlinProvidedDispatchers.IO
    override val cpu: CoroutineDispatcher get() = KotlinProvidedDispatchers.Default
    override val unconfined: CoroutineDispatcher get() = KotlinProvidedDispatchers.Unconfined
    override val ui: CoroutineDispatcher get() = KotlinProvidedDispatchers.Main.immediate
}
