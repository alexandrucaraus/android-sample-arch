package eu.acaraus.shared.test.lib.di


import eu.acaraus.shared.lib.coroutines.DispatcherProvider
import eu.acaraus.shared.lib.coroutines.DispatcherProviderApp
import org.koin.core.context.GlobalContext
import org.koin.dsl.module

fun setupUnitTestDi(mockModules: List<org.koin.core.module.Module> = emptyList()) {
    val allModules =
        mockModules + module { single<DispatcherProvider> { DispatcherProviderApp() } }
    GlobalContext.startKoin {
        modules(allModules)
    }
}
