package com.germanautolabs.acaraus.test.common.di

import com.germanautolabs.acaraus.main.ScreensDi
import com.germanautolabs.acaraus.main.UsecaseDi
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.context.GlobalContext
import org.koin.ksp.generated.module

fun setupUnitTestDi(mockModules: List<org.koin.core.module.Module> = emptyList()) {
    val allModules = mockModules + listOf(UsecaseDi().module, ScreensDi().module, TestDi().module)
    GlobalContext.startKoin {
        modules(allModules)
    }
}

@Module
@ComponentScan(value = "com.germanautolabs.acaraus.test.common.coroutines")
class TestDi
