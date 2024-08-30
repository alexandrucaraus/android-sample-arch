package eu.acaraus.news.test.common.di


import eu.acaraus.news.di.DomainDi
import eu.acaraus.news.di.PresentationDi
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.context.GlobalContext
import org.koin.ksp.generated.module

fun setupUnitTestDi(mockModules: List<org.koin.core.module.Module> = emptyList()) {
    val allModules =
        mockModules + listOf(DomainDi().module, PresentationDi().module, TestDi().module)
    GlobalContext.startKoin {
        modules(allModules)
    }
}

@Module
@ComponentScan(value = "eu.acaraus.news.test.common.coroutines")
class TestDi
