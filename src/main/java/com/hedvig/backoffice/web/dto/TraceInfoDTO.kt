package com.hedvig.backoffice.web.dto

import lombok.Data

import java.time.LocalDateTime
import java.util.UUID

data class TraceInfoDTO(
    val date: LocalDateTime,
    val oldValue: String?,
    val newValue: String,
    val fieldName: String,
    val productId: UUID,
    val memberId: Long,
    val userId: String,
    val isSuccess: Boolean
)
