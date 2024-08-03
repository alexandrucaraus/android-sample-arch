package com.germanautolabs.acaraus.usecase

import com.germanautolabs.acaraus.data.LocaleStore
import org.koin.core.annotation.Factory

@Factory
class GetLocale(
    private val localeStore: LocaleStore,
) {
    fun languageCode(): String = localeStore.getLanguageCode()
    fun countryCode(): String = localeStore.getCountryCode()
}
