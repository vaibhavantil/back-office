package com.hedvig.backoffice.services.product_pricing.dto.contract

import com.fasterxml.jackson.annotation.JsonProperty
import com.hedvig.backoffice.graphql.types.Member
import com.hedvig.backoffice.services.underwriter.dtos.QuoteInitiatedFrom
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import javax.money.CurrencyUnit

data class Contract(
    val id: UUID,
    val holderMemberId: String,
    val switchedFrom: String?,
    val masterInception: LocalDate?,
    val status: ContractStatus,
    val typeOfContract: String,
    @get:JsonProperty("isTerminated")
    val isTerminated: Boolean,
    val terminationDate: LocalDate?,
    val currentAgreementId: UUID,
    val hasPendingAgreement: Boolean,
    val genericAgreements: List<GenericAgreement>,
    val hasQueuedRenewal: Boolean,
    val renewal: Renewal?,
    val preferredCurrency: CurrencyUnit,
    val market: Market,
    val signSource: QuoteInitiatedFrom?,
    val contractTypeName: String,
    val createdAt: Instant
) {
    lateinit var holderMember: Member

    val holderFirstName: String?
        get() = holderMember.firstName

    val holderLastName: String?
        get() = holderMember.lastName
}
