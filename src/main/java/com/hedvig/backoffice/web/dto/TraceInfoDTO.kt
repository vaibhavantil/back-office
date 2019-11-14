package com.hedvig.backoffice.web.dto

import java.time.LocalDateTime
import java.util.UUID

data class TraceInfoDTO(
    val date: LocalDateTime? = null,
    val oldValue: String? = null,
    val newValue: String? = null,
    val fieldName: String? = null,
    val productId: UUID? = null,
    val memberId: Long? = null,
    val userId: String? = null,
    val isSuccess: Boolean? = null
)
