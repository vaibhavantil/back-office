package com.hedvig.backoffice.services.product_pricing.dto.contract

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.hedvig.backoffice.graphql.UnionType
import com.hedvig.graphql.commons.type.MonetaryAmountV2
import java.time.LocalDate
import java.util.*
import javax.money.MonetaryAmount

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(value = Agreement.SwedishApartment::class, name = "SwedishApartment"),
  JsonSubTypes.Type(value = Agreement.SwedishHouse::class, name = "SwedishHouse"),
  JsonSubTypes.Type(value = Agreement.NorwegianHomeContent::class, name = "NorwegianHomeContent"),
  JsonSubTypes.Type(value = Agreement.NorwegianTravel::class, name = "NorwegianTravel")
)
@UnionType
sealed class Agreement {
  abstract val id: UUID
  abstract val fromDate: LocalDate?
  abstract val toDate: LocalDate?
  abstract val basePremium: MonetaryAmount
  abstract val certificateUrl: String?
  abstract val status: AgreementStatus

  @UnionType
  data class SwedishApartment(
    override val id: UUID,
    override val fromDate: LocalDate?,
    override val toDate: LocalDate?,
    override val basePremium: MonetaryAmount,
    override val certificateUrl: String?,
    override val status: AgreementStatus,
    val lineOfBusiness: SwedishApartmentLineOfBusiness,
    val address: Address,
    val numberCoInsured: Int,
    val squareMeters: Long
  ) : Agreement()

  @UnionType
  data class SwedishHouse(
    override val id: UUID,
    override val fromDate: LocalDate?,
    override val toDate: LocalDate?,
    override val basePremium: MonetaryAmount,
    override val certificateUrl: String?,
    override val status: AgreementStatus,
    val address: Address,
    val numberCoInsured: Int,
    val squareMeters: Long,
    val ancillaryArea: Long,
    val yearOfConstruction: Int,
    val numberOfBathrooms: Int,
    val extraBuildings: List<ExtraBuilding>,
    val isSubleted: Boolean
  ) : Agreement()

  @UnionType
  data class NorwegianHomeContent(
    override val id: UUID,
    override val fromDate: LocalDate?,
    override val toDate: LocalDate?,
    override val basePremium: MonetaryAmount,
    override val certificateUrl: String?,
    override val status: AgreementStatus,
    val lineOfBusiness: NorwegianHomeContentLineOfBusiness,
    val address: Address,
    val numberCoInsured: Int,
    val squareMeters: Long
  ) : Agreement()

  @UnionType
  data class NorwegianTravel(
    override val id: UUID,
    override val fromDate: LocalDate?,
    override val toDate: LocalDate?,
    override val basePremium: MonetaryAmount,
    override val certificateUrl: String?,
    override val status: AgreementStatus,
    val lineOfBusiness: NorwegianTravelLineOfBusiness,
    val numberCoInsured: Int
  ) : Agreement()

  val premium: MonetaryAmountV2
    get() = MonetaryAmountV2.of(basePremium)
}
