package com.hedvig.backoffice.services.members.dto

import com.neovisionaries.i18n.CountryCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class PickedLocaleDTOTest {
    @Test
    fun extractsCorrectCountryCode() {
        val swedishSwedenPickedLocale = PickedLocaleDTO("sv_SE")
        val englishSwedenPickedLocale = PickedLocaleDTO("en_SE")
        val norwegianNorwayPickedLocale = PickedLocaleDTO("nb_NO")
        val englishNorwayPickedLocale = PickedLocaleDTO("en_NO")
        val danishDenmarkPickedLocale = PickedLocaleDTO("da_DK")
        val englishDenmarkPickedLocale = PickedLocaleDTO("en_DK")

        assertThat(swedishSwedenPickedLocale.countryCode).isEqualTo(CountryCode.SE)
        assertThat(englishSwedenPickedLocale.countryCode).isEqualTo(CountryCode.SE)
        assertThat(norwegianNorwayPickedLocale.countryCode).isEqualTo(CountryCode.NO)
        assertThat(englishNorwayPickedLocale.countryCode).isEqualTo(CountryCode.NO)
        assertThat(danishDenmarkPickedLocale.countryCode).isEqualTo(CountryCode.DK)
        assertThat(englishDenmarkPickedLocale.countryCode).isEqualTo(CountryCode.DK)
    }
}
