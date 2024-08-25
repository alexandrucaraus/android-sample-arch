package com.germanautolabs.acaraus.test.main.rules

import com.germanautolabs.acaraus.infra.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlinx.coroutines.Dispatchers as KotlinDispatchers

class CoroutinesTestRule : TestWatcher(), KoinTest {

    val testDispatcher by inject<Dispatchers>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description) {
        KotlinDispatchers.setMain(testDispatcher.ui)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description) {
        KotlinDispatchers.resetMain()
    }
}
