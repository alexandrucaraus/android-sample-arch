package com.germanautolabs.acaraus.test.common.rules

import com.germanautolabs.acaraus.test.common.di.setupUnitTestDi
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.module.Module

/**
Starts and stops koin before each test, with custom modules
 */
class KoinUnitTestRule(private vararg val mockModules: Module) : TestWatcher() {
    override fun starting(description: Description) {
        try {
            setupUnitTestDi(mockModules.toList())
        } catch (ignore: Exception) {
        }
    }

    override fun finished(description: Description) {
        stopKoin()
    }
}
