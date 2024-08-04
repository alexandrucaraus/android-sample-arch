package com.germanautolabs.acaraus.infra

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Single
import kotlinx.coroutines.Dispatchers as KotlinProvidedDispatchers

interface Dispatchers {
    val io: CoroutineDispatcher
    val cpu: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
    val ui: CoroutineDispatcher
}

@Single(binds = [Dispatchers::class])
class DispatchersApp : Dispatchers {
    override val io: CoroutineDispatcher get() = KotlinProvidedDispatchers.IO
    override val cpu: CoroutineDispatcher get() = KotlinProvidedDispatchers.Default
    override val unconfined: CoroutineDispatcher get() = KotlinProvidedDispatchers.Unconfined
    override val ui: CoroutineDispatcher get() = KotlinProvidedDispatchers.Main.immediate
}
