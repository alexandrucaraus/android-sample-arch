package com.germanautolabs.acaraus.test.rules

import com.germanautolabs.acaraus.main.MainDi
import com.germanautolabs.acaraus.test.DispatchersTestDi
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.ksp.generated.module

class KoinUnitTestRule : TestWatcher() {
    override fun starting(description: Description) {
        try {
            setupUnitTestDi()
        } catch (ignore: Exception) {
        }
    }

    override fun finished(description: Description) {
        stopKoin()
    }
}

fun setupUnitTestDi() {
    startKoin {
        modules(
            MainDi().module,
            DispatchersTestDi().module,
        )
    }
}
