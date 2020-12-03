package com.hedvig.backoffice.services.members.dto

import com.neovisionaries.i18n.CountryCode


data class PickedLocaleDTO(
    val pickedLocale: String
) {
    val countryCode: CountryCode
        get() = CountryCode.getByAlpha2Code(pickedLocale.takeLast(2))
}
