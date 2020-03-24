package com.hedvig.backoffice.services.product_pricing.dto.contract

import com.fasterxml.jackson.annotation.JsonProperty
import com.hedvig.backoffice.services.underwriter.dtos.QuoteInitiatedFrom
import java.time.Instant
import java.time.LocalDate
import java.util.*
import javax.money.CurrencyUnit

data class Contract(
  val id: UUID,
  val holderMemberId: String,
  val switchedFrom: String?,
  val masterInception: LocalDate?,
  val status: ContractStatus,
  @get:JsonProperty("isTerminated")
  val isTerminated: Boolean,
  val terminationDate: LocalDate?,
  val currentAgreementId: UUID,
  val hasPendingAgreement: Boolean,
  val agreements: List<Agreement>,
  val hasQueuedRenewal: Boolean,
  val renewal: Renewal?,
  val preferredCurrency: CurrencyUnit,
  val market: Market,
  val signSource: QuoteInitiatedFrom?,
  val contractTypeName: String,
  val createdAt: Instant
)
