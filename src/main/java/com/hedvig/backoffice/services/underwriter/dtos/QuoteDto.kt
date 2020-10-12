package com.hedvig.backoffice.services.underwriter.dtos

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.*

enum class ProductType {
    APARTMENT,
    HOUSE,
    OBJECT,
    HOME_CONTENT,
    TRAVEL,
    UNKNOWN
}

enum class QuoteState {
    INCOMPLETE,
    QUOTED,
    SIGNED,
    EXPIRED
}

enum class QuoteInitiatedFrom {
    RAPIO,
    WEBONBOARDING,
    WEB,
    APP,
    IOS,
    ANDROID,
    HOPE
}

data class QuoteDto(
    val id: UUID,
    val createdAt: Instant,
    val price: BigDecimal? = null,
    val productType: ProductType,
    val state: QuoteState,
    val initiatedFrom: QuoteInitiatedFrom,
    val attributedTo: String,
    val currentInsurer: String? = null,
    val startDate: LocalDate? = null,
    val validity: Long,
    val memberId: String? = null,
    val breachedUnderwritingGuidelines: List<String>?,
    val isComplete: Boolean,
    val signedProductId: UUID?,
    val originatingProductId: UUID?
)
