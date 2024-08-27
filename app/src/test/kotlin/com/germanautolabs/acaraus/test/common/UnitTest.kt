package com.germanautolabs.acaraus.test.common

import com.germanautolabs.acaraus.test.common.rules.CoroutinesTestRule
import com.germanautolabs.acaraus.test.common.rules.KoinUnitTestRule
import org.junit.Rule
import org.koin.test.KoinTest

interface UnitTest : KoinTest {

    @get:Rule
    val koinUnitTestRule get() = KoinUnitTestRule()

    @get:Rule
    val coroutinesTestRule get() = CoroutinesTestRule()
}
