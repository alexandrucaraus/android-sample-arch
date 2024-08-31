package eu.acaraus.news.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module
import org.koin.core.module.Module as KoinDiModule

val newsDiModules: List<KoinDiModule> = listOf(
    DataDi().module,
    DomainDi().module,
    PresentationDi().module,
)

@Module
@ComponentScan(value = "eu.acaraus.news.data")
class DataDi

@Module
@ComponentScan(value = "eu.acaraus.news.domain")
class DomainDi

@Module
@ComponentScan(value = "eu.acaraus.news.presentation")
class PresentationDi
