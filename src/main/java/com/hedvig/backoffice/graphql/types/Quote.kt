package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.*

enum class QuoteState {
    INCOMPLETE,
    QUOTED,
    SIGNED,
    EXPIRED
}

data class Quote(
    val id: UUID,
    val createdAt: Instant,
    val price: BigDecimal? = null,
    val currency: String,
    val productType: String,
    val state: QuoteState,
    val initiatedFrom: String,
    val attributedTo: String,
    val currentInsurer: String? = null,

    val startDate: LocalDate? = null,
    val validity: Long,
    val memberId: String? = null,
    val breachedUnderwritingGuidelines: List<String>?,
    val isComplete: Boolean,
    val signedProductId: UUID?,
    val originatingProductId: UUID?,
    val isReadyToSign: Boolean = false
) {
    companion object {
        @JvmStatic
        fun from(quote: QuoteDto): Quote =
            Quote(
                id = quote.id,
                currentInsurer = quote.currentInsurer,
                memberId = quote.memberId,
                startDate = quote.startDate,
                price = quote.price,
                currency = quote.currency,
                state = QuoteState.valueOf(quote.state.toString()),
                productType = quote.productType,
                attributedTo = quote.attributedTo,
                initiatedFrom = quote.initiatedFrom.toString(),
                createdAt = quote.createdAt,
                validity = quote.validity,
                isComplete = quote.isComplete,
                breachedUnderwritingGuidelines = quote.breachedUnderwritingGuidelines,
                originatingProductId = quote.originatingProductId,
                signedProductId = quote.signedProductId
            )
    }
}
