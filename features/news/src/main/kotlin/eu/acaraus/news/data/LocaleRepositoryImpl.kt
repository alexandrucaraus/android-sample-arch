package eu.acaraus.news.data

import eu.acaraus.news.domain.repositories.LocaleRepository
import org.koin.core.annotation.Single
import java.util.Locale

@Single(binds = [LocaleRepository::class])
class LocaleRepositoryImpl : LocaleRepository {

    private var languageCode = Locale.getDefault().language
    private var countryCode = Locale.getDefault().country

    @Synchronized
    override fun setLanguageCode(code: String) {
        languageCode = code
    }

    @Synchronized
    override fun getLanguageCode(): String = languageCode

    @Synchronized
    override fun setCountryCode(code: String) {
        countryCode = code
    }

    @Synchronized
    override fun getCountryCode(): String = countryCode
}
