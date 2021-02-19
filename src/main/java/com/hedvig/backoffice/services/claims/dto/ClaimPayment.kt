package com.hedvig.backoffice.services.claims.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.hedvig.backoffice.services.payments.dto.SelectedPayoutDetails
import com.hedvig.backoffice.util.DoubleOrMonetaryAmountToMonetaryAmountDeserializer
import lombok.AllArgsConstructor
import lombok.Data
import java.time.LocalDateTime
import java.util.UUID
import javax.money.MonetaryAmount
import javax.validation.constraints.NotNull

data class ClaimPayment(
    var claimId: String,
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE) @field:JsonDeserialize(using = DoubleOrMonetaryAmountToMonetaryAmountDeserializer::class) var amount: @NotNull MonetaryAmount?,
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE) @field:JsonDeserialize(using = DoubleOrMonetaryAmountToMonetaryAmountDeserializer::class) var deductible: @NotNull MonetaryAmount?,
    val note: String,
    val exGratia: Boolean,
    val type: ClaimPaymentType,
    var handlerReference: String,
    val sanctionListSkipped: Boolean,
    val payoutDetails: SelectedPayoutDetails,
    //The following fields are used when getting a claim, and are null when creating a payment:
    @JsonProperty("id")
    var paymentId: String? = null,
    @JsonProperty("date")
    var registrationDate: LocalDateTime? = null,
    var payoutStatus: ClaimPaymentStatus? = null,
    var transactionId: UUID? = null
)
