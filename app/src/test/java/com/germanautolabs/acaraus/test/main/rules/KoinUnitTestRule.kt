package com.germanautolabs.acaraus.test.main.rules

import com.germanautolabs.acaraus.test.main.setupUnitTestDi
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.module.Module

/**
Starts and stops koin before each test, with custom modules
 */
class KoinUnitTestRule(private val mockModules: List<Module> = emptyList()) : TestWatcher() {
    override fun starting(description: Description) {
        try {
            setupUnitTestDi(mockModules)
        } catch (ignore: Exception) {
        }
    }

    override fun finished(description: Description) {
        stopKoin()
    }
}
