package com.hedvig.backoffice.web.dto

import java.time.LocalDateTime
import java.util.UUID

data class TraceInfoDTO(
    val date: LocalDateTime,
    val oldValue: String? = null,
    val newValue: String,
    val fieldName: String,
    val productId: UUID? = null,
    val memberId: Long,
    val userId: String,
    val isSuccess: Boolean
)
