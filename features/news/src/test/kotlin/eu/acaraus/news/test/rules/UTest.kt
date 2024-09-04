package eu.acaraus.news.test.rules

import eu.acaraus.news.di.DomainDi
import eu.acaraus.news.di.PresentationDi
import org.koin.core.module.Module
import org.koin.ksp.generated.module
import eu.acaraus.shared.test.lib.UnitTest as SharedUnitTest

interface UTest : SharedUnitTest {
    override val perFeatureModules: Array<Module>
        get() = arrayOf(
            DomainDi().module,
            PresentationDi().module,
        )
}
