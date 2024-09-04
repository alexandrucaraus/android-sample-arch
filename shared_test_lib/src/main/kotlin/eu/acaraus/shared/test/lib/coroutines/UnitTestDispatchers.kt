@file:Suppress("All")

package eu.acaraus.shared.test.lib.coroutines

import eu.acaraus.shared.lib.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.core.annotation.Single

@Single(binds = [DispatcherProvider::class])
class UnitTestDispatchers : DispatcherProvider {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())

    override val io: CoroutineDispatcher = dispatcher
    override val cpu: CoroutineDispatcher = dispatcher
    override val unconfined: CoroutineDispatcher = dispatcher
    override val ui: CoroutineDispatcher = dispatcher
}
