package eu.acaraus.shared.test.lib


import eu.acaraus.shared.test.lib.rules.CoroutinesTestRule
import eu.acaraus.shared.test.lib.rules.KoinUnitTestRule
import org.junit.Rule
import org.koin.core.module.Module
import org.koin.test.KoinTest

interface UnitTest : KoinTest {

    val modules: Array<Module> get() = emptyArray<Module>()

    @get:Rule
    val koinUnitTestRule get() = KoinUnitTestRule(*modules, *testModules())

    @get:Rule
    val coroutinesTestRule get() = CoroutinesTestRule()

    fun testModules(): Array<Module> = emptyArray()
}
