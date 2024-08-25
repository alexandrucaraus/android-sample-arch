@file:Suppress("All")

package com.germanautolabs.acaraus.test.infra

import com.germanautolabs.acaraus.infra.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.core.annotation.Single

@Single(binds = [Dispatchers::class])
class DispatchersUnitTest : Dispatchers {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())

    override val io: CoroutineDispatcher = dispatcher
    override val cpu: CoroutineDispatcher = dispatcher
    override val unconfined: CoroutineDispatcher = dispatcher
    override val ui: CoroutineDispatcher = dispatcher
}
