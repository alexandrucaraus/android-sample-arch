package eu.acaraus.shared.test.lib.di

import org.koin.core.context.GlobalContext

fun setupUnitTestDi(mockModules: List<org.koin.core.module.Module> = emptyList()) {
    GlobalContext.startKoin {
        modules(mockModules)
    }
}
