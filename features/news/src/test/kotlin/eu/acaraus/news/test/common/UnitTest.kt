package eu.acaraus.news.test.common

import eu.acaraus.news.test.common.rules.CoroutinesTestRule
import eu.acaraus.news.test.common.rules.KoinUnitTestRule
import org.junit.Rule
import org.koin.test.KoinTest

interface UnitTest : KoinTest {

    @get:Rule
    val koinUnitTestRule get() = KoinUnitTestRule()

    @get:Rule
    val coroutinesTestRule get() = CoroutinesTestRule()
}
