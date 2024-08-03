package com.germanautolabs.acaraus.usecase

import com.germanautolabs.acaraus.data.LocaleStore
import org.koin.core.annotation.Factory

@Factory
class SetLocale(
    private val localeStore: LocaleStore,
) {
    fun languageCode(code: String) {
        localeStore.setLanguageCode(code)
    }
    fun countryCode(code: String) {
        localeStore.setCountryCode(code)
    }
}
