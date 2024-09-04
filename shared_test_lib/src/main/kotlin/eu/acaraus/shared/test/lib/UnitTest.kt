package eu.acaraus.shared.test.lib

import eu.acaraus.shared.lib.coroutines.DispatcherProvider
import eu.acaraus.shared.test.lib.coroutines.DispatcherProviderTest
import eu.acaraus.shared.test.lib.rules.CoroutinesTestRule
import eu.acaraus.shared.test.lib.rules.KoinUnitTestRule
import org.junit.Rule
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest

interface UnitTest : KoinTest {

    private fun commonModule() = module { single<DispatcherProvider> { DispatcherProviderTest() } }

    val perFeatureModules: Array<Module> get() = emptyArray<Module>()
    fun perTestModules(): Array<Module> = emptyArray()

    @get:Rule
    val koinUnitTestRule
        get() = KoinUnitTestRule(
            commonModule(),
            *perFeatureModules,
            *perTestModules(),
        )

    @get:Rule
    val coroutinesTestRule get() = CoroutinesTestRule()
}
