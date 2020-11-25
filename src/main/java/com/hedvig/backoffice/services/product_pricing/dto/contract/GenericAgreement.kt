package com.hedvig.backoffice.services.product_pricing.dto.contract

import com.hedvig.graphql.commons.type.MonetaryAmountV2
import java.time.LocalDate
import java.util.UUID
import javax.money.MonetaryAmount

data class GenericAgreement(
    val id: UUID,
    val fromDate: LocalDate?,
    val toDate: LocalDate?,
    val basePremium: MonetaryAmount,
    val certificateUrl: String?,
    val status: AgreementStatus,
    val typeOfContract: String,
    val address: Address?,
    val numberCoInsured: Int?,
    val squareMeters: Long?,
    val ancillaryArea: Long?,
    val yearOfConstruction: Int?,
    val numberOfBathrooms: Int?,
    val extraBuildings: List<ExtraBuilding>?,
    val isSubleted: Boolean?,
    val lineOfBusinessName: String
) {
    val premium: MonetaryAmountV2
        get() = MonetaryAmountV2.of(basePremium)
}
